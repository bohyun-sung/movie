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
import com.toyproject.movie.core.domain.schedule.ScheduleSeatLog;
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
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createReservation(ReservationCreateReq req) {
        // 좌석 idx
        List<Long> ssIdxs = req.reservationSeatDtos().stream()
                .map(ReservationSeatDto::ssIdx)
                .toList();
        // 상영관 좌석 조회 및 좌석 점유 검증
        List<ScheduledSeat> scheduledSeatList = validateAndGetScheduledSeats(ssIdxs);
        // 상영관 정보
        Schedule schedule = scheduleRepository.findById(req.scheduleIdx())
                .orElseThrow(() -> new ClientException(ExceptionType.NOT_FOUND, "존재하지않는 상영관입니다."));
        // 고객 정보
        Client client = clientRepository.findById(req.clientIdx())
                .orElseThrow(() -> new ClientException(ExceptionType.NOT_FOUND, "존재하지않는 고객입니다."));
        // TODO 날짜 + 상영관 + 상영일정 + 좌석
        String reservationNumber = LocalDateTime.now() + client.getClientIdx().toString();
        // 예매 | 예매 상세 | 상영관 좌석 기록 entity 생성 및 저장
        Reservation saveReservation = saveReservation(req, schedule, reservationNumber, client);
        saveReservationDetails(req, scheduledSeatList, schedule, saveReservation);
        saveScheduleSeatLog(scheduledSeatList, client);
    }

    /**
     * 상영관 좌석 상태 로그 entity 생성 및 저장
     *
     * @param scheduledSeatList 좌석 상태 entity 리스트
     * @param client            고객 entity
     */
    private void saveScheduleSeatLog(List<ScheduledSeat> scheduledSeatList, Client client) {
        List<ScheduleSeatLog> scheduleSeatLogList = scheduledSeatList.stream()
                .map(scheduledSeat ->
                        ScheduleSeatLog.of(
                                client.getClientIdx(),
                                SeatReservationStatus.PENDING,
                                "결제전 점유",
                                scheduledSeat)
                ).toList();

        scheduleSeatLogRepository.saveAll(scheduleSeatLogList);
    }

    /**
     * 예매 상세 저장
     *
     * @param req               요청받은 객체
     * @param scheduledSeatList 점유할려는 좌석 리스트
     * @param schedule          상영일정
     * @param saveReservation   예매 entity
     */
    private void saveReservationDetails(ReservationCreateReq req, List<ScheduledSeat> scheduledSeatList, Schedule schedule, Reservation saveReservation) {
        // 예약 상세 리스트 | ReservationSeatDto 에서 <ssIdx, AudienceDiscountType> 담을 Map 생성 및 put
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

        reservationDetailRepository.saveAll(reservationDetails);
    }

    /**
     * 예매 엔티티 생성 및 저장
     *
     * @param req               req
     * @param schedule          상영일정
     * @param reservationNumber 예매번호
     * @param client            고객
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
     *
     * @param ssIdxs 상영관 좌석 idx
     * @return List<ScheduledSeat>
     */
    private List<ScheduledSeat> validateAndGetScheduledSeats(List<Long> ssIdxs) {
        List<ScheduledSeat> scheduledSeatList = scheduledSeatRepository.findAllByIdWithLock(ssIdxs);

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
