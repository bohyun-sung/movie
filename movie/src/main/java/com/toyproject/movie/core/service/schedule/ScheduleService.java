package com.toyproject.movie.core.service.schedule;

import com.toyproject.movie.api.dto.schedule.request.ScheduleCreateReq;
import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.core.domain.movie.Movie;
import com.toyproject.movie.core.domain.schedule.Schedule;
import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.core.domain.theater.Seat;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.core.repository.movie.MovieRepository;
import com.toyproject.movie.core.repository.schedule.ScheduleRepository;
import com.toyproject.movie.core.repository.schedule.jdbc.ScheduledSeatJdbcRepository;
import com.toyproject.movie.core.repository.theater.SeatRepository;
import com.toyproject.movie.core.repository.theater.TheaterRepository;
import com.toyproject.movie.global.enums.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.toyproject.movie.global.enums.SeatReservationStatus.AVAILABLE;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final ScheduledSeatJdbcRepository seatJdbcRepository;


    @Transactional
    public void createSchedule(ScheduleCreateReq req) {
        // 영화 객체 조회
        Movie movie = movieRepository.findById(req.movieIdx())
                .orElseThrow(() -> new ClientException(ExceptionType.NOT_FOUND, "해당 영화 정보를 찾을 수 없습니다."));

        // 상영관 검증 && 프록시 객체 조회
        if (!theaterRepository.existsById(req.theaterIdx())) {
            throw new ClientException(ExceptionType.NOT_FOUND, "해당 상영관 정보를 찾을 수 없습니다");
        }
        Theater referenceTheater = theaterRepository.getReferenceById(req.theaterIdx());

        Schedule schedule = req.toEntity(movie, referenceTheater);
        // 상영관 일정 검증
        if (scheduleRepository.existsOverlappingSchedule(req.theaterIdx(), schedule.getStartAt(), schedule.getEndAt())) {
            throw new ClientException(ExceptionType.CONFLICT, "해당 시간때 이미 상영관에 상영일정이 등록 되어있습니다.");
        }
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // 좌석 조회
        List<Seat> seats = seatRepository.findAllByTheaterIdx(req.theaterIdx());
        // 상영관 좌석 상태 생성 및 저장
        List<ScheduledSeat> scheduledSeatList = seats.stream()
                .map(seat -> ScheduledSeat.of(
                        AVAILABLE,
                        null,
                        saveSchedule,
                        seat))
                .toList();
        seatJdbcRepository.batchInsertScheduledSeat(scheduledSeatList);
    }
}
