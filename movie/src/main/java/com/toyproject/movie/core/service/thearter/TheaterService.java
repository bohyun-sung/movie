package com.toyproject.movie.core.service.thearter;

import com.toyproject.movie.api.dto.theater.request.TheaterCreateReq;
import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.core.domain.theater.Seat;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.core.repository.theater.SeatRepository;
import com.toyproject.movie.core.repository.theater.TheaterRepository;
import com.toyproject.movie.core.repository.theater.jdbc.SeatJdbcRepository;
import com.toyproject.movie.global.enums.ExceptionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final SeatJdbcRepository seatJdbcRepository;

    @Transactional
    public void createTheater(TheaterCreateReq req) {
        // 상영관 이름 중복
        if (theaterRepository.existsByCinemaNameAndTheaterName(req.cinemaName(), req.theaterName())) {
            throw new ClientException(ExceptionType.CONFLICT, "이미 해당 영화관에 동일한 이름의 상영관이 존재합니다.");
        }
        // 행 좌석
        char row = req.lastSeatRow().toUpperCase(Locale.ROOT).charAt(0);
        List<String> totalSeatRow = IntStream.rangeClosed('A', row)
                .mapToObj(seatRow -> String.valueOf((char) seatRow))
                .toList();
        // 열 좌석
        List<Integer> totalSeatColum = IntStream.rangeClosed(1, req.lastSeatColum())
                .boxed()
                .toList();
        // 전체 좌석 수
        int seatCount = totalSeatRow.size() * totalSeatColum.size();
        // 상영관 저장 && 생성
        Theater theater = req.toTheaterEntity(seatCount);
        Theater saveTheater = theaterRepository.save(theater);

        // 좌석 저장 && 생성
        List<Seat> seatList = totalSeatRow.stream()
                .flatMap(seatRow -> totalSeatColum.stream()
                        .map(seatColum -> Seat.of(seatRow, seatColum, saveTheater)))
                .toList();

//        seatRepository.saveAll(seatList);
        seatJdbcRepository.batchInsertSeats(seatList);
    }
}
