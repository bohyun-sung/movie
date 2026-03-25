package com.toyproject.movie.core.service.reservation;

import com.toyproject.movie.api.dto.reservation.ReservationSeatDto;
import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.common.exception.ServerException;
import com.toyproject.movie.common.utill.ReservationUtil;
import com.toyproject.movie.core.domain.client.Client;
import com.toyproject.movie.core.domain.reservation.Reservation;
import com.toyproject.movie.core.domain.reservation.ReservationDetail;
import com.toyproject.movie.core.domain.schedule.Schedule;
import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.core.repository.client.ClientRepository;
import com.toyproject.movie.core.repository.reservation.ReservationDetailRepository;
import com.toyproject.movie.core.repository.reservation.ReservationRepository;
import com.toyproject.movie.core.repository.schedule.ScheduleRepository;
import com.toyproject.movie.core.repository.schedule.ScheduleSeatLogRepository;
import com.toyproject.movie.core.repository.schedule.ScheduledSeatRepository;
import com.toyproject.movie.global.enums.AudienceDiscountType;
import com.toyproject.movie.global.enums.ExceptionType;
import com.toyproject.movie.global.enums.ReservationDetailStatus;
import com.toyproject.movie.global.enums.SeatReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final RedisCacheService redisCacheService;

    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduledSeatRepository scheduledSeatRepository;
    private final ScheduleSeatLogRepository scheduleSeatLogRepository;

    @Transactional
    public void createReservation(ReservationCreateReq req) {
        // 좌석 idx
        List<Long> ssIdxs = req.reservationSeatDtos().stream()
                .map(ReservationSeatDto::ssIdx)
                .toList();
        // redis 좌석 키
        List<String> occupiedKeys = new ArrayList<>();
        try {
            // redis 좌석 점유 확인 (분산락)
            req.reservationSeatDtos().forEach(reservationSeatDto -> {
                String occupiedKey = redisCacheService.occupySeat(req.scheduleIdx(), req.clientIdx(), reservationSeatDto.ssIdx());
                occupiedKeys.add(occupiedKey);
            });
            // 좌석 존재 여부 상태 확인
            List<ScheduledSeat> scheduledSeatList = validateAndGetScheduledSeats(ssIdxs);
            // 상영관 정보
            Schedule schedule = scheduleRepository.findById(req.scheduleIdx())
                    .orElseThrow(() -> new ClientException(ExceptionType.NOT_FOUND, "존재하지않는 상영관입니다."));
            // TODO 날짜 + 상영관 + 상영일정 + 좌석
            String reservationNumber = LocalDateTime.now().toString();
            // 고객 정보
            Client client = clientRepository.findById(req.clientIdx())
                    .orElseThrow(() -> new ClientException(ExceptionType.NOT_FOUND, "존재하지않는 고객입니다."));
            // 예매 entity 생성 및 저장
            Reservation saveReservation = saveReservation(req, schedule, reservationNumber, client);
            // 예매 상세 생성 및 저장
            saveReservationDetails(req, scheduledSeatList, schedule, saveReservation, ssIdxs);
        } catch (Exception e) {
            log.error("예매 실패로 인한 redis 롤백 수행, occupiedKeys: {}, reason: {}", occupiedKeys, e.getMessage());
            redisCacheService.rollBackSeats(occupiedKeys);
            throw e;
        }
    }

    private void saveReservationDetails(ReservationCreateReq req, List<ScheduledSeat> scheduledSeatList, Schedule schedule, Reservation saveReservation, List<Long> ssIdxs) {
        List<ReservationDetail> reservationDetails = new ArrayList<>();
        Map<Long, AudienceDiscountType> audienceDiscountTypeMap = new HashMap<>();

        req.reservationSeatDtos().forEach(dto -> {
            audienceDiscountTypeMap.put(dto.ssIdx(), dto.audienceDiscountType());
        });
        // 예매 상세 entity 생성
        scheduledSeatList.forEach(scheduledSeat -> {
            AudienceDiscountType audienceDiscountType = audienceDiscountTypeMap.get(scheduledSeat.getSsIdx());
            Integer price = ReservationUtil.totalDiscount(schedule.discountTicketPrice(), audienceDiscountType);
            ReservationDetail reservationDetail = ReservationDetail.of(
                    price,
                    audienceDiscountType,
                    scheduledSeat.getSeat().getFullSeatNumber(),
                    ReservationDetailStatus.PENDING,
                    saveReservation,
                    scheduledSeat);
            reservationDetails.add(reservationDetail);
        });

        if (reservationDetails.size() != ssIdxs.size()) {
            log.error("reservationDetails : {}", reservationDetails);
            throw new ServerException(ExceptionType.INTERNAL_SERVER_ERROR, "예매에 실패하였습니다.");
        }
        reservationDetailRepository.saveAll(reservationDetails);
    }

    /**
     * 예매 엔티티 생성 및 저장
     * @param req                   req
     * @param schedule              상영일정
     * @param reservationNumber     예매번호
     * @param client                고객
     * @return Reservation
     */
    private Reservation saveReservation(ReservationCreateReq req, Schedule schedule, String reservationNumber, Client client) {
        Reservation reservation = req.toReservationEntity(
                schedule.discountTicketPrice(),
                schedule.getMovie().getMovieTitle(),
                schedule.getTheater().getCinemaTheaterName(),
                reservationNumber,
                client);
        // 예매 저장
        return reservationRepository.save(reservation);
    }

    /**
     * 상영관 좌석 조회 및 좌석 점유 검증
     * @param ssIdxs 상영관 좌석 idx
     * @return List<ScheduledSeat>
     */
    private List<ScheduledSeat> validateAndGetScheduledSeats(List<Long> ssIdxs) {
        List<ScheduledSeat> scheduledSeatList = scheduledSeatRepository.findAllById(ssIdxs);

        if (scheduledSeatList.size() != ssIdxs.size()) {
            throw new ClientException(ExceptionType.BAD_REQUEST, "존재하지않는 좌석입니다.");
        }

        scheduledSeatList.forEach(scheduledSeat -> {
            if (scheduledSeat.getSeatReservationStatus() != SeatReservationStatus.AVAILABLE) {
                throw new ClientException(ExceptionType.BAD_REQUEST, "이미 예약된 좌석입니다.");
            }
            scheduledSeat.reserver();
        });
        return scheduledSeatList;
    }

}
