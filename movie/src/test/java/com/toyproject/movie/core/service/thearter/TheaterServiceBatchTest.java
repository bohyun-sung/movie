package com.toyproject.movie.core.service.thearter;

import com.toyproject.movie.core.domain.theater.Seat;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.core.repository.theater.SeatRepository;
import com.toyproject.movie.core.repository.theater.TheaterRepository;
import com.toyproject.movie.core.repository.theater.jdbc.SeatJdbcRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test") // 테스트용 환경 설정 사용
class TheaterServiceBatchTest {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatJdbcRepository seatJdbcRepository;

    @Test
    @DisplayName("JPA saveAll vs Jdbc batchInsert 성능 비교")
    void compareInsertPerformance() {
        // Given: 10x20 크기의 상영관 좌석 데이터 (200개) 생성 준비
        Theater theater = Theater.of("강남", "1관", 200);
        Theater savedTheater = theaterRepository.save(theater);

        List<Seat> seatList = new ArrayList<>();
        for (char row = 'A'; row <= 'J'; row++) {
            for (int col = 1; col <= 20; col++) {
                seatList.add(Seat.of(String.valueOf(row), col, savedTheater));
            }
        }

        // JPA saveAll 측정
        StopWatch jpaStopWatch = new StopWatch();
        jpaStopWatch.start();
        seatRepository.saveAll(seatList);
        jpaStopWatch.stop();
        System.out.println("JPA saveAll 소요 시간: " + jpaStopWatch.getTotalTimeMillis() + "ms");

        // 데이터 삭제 후 재준비 (정확한 비교를 위해)
        seatRepository.deleteAllInBatch();

        // JdbcTemplate batchInsert 측정
        StopWatch jdbcStopWatch = new StopWatch();
        jdbcStopWatch.start();
        seatJdbcRepository.batchInsertSeats(seatList);
        jdbcStopWatch.stop();
        System.out.println("JDBC BatchInsert 소요 시간: " + jdbcStopWatch.getTotalTimeMillis() + "ms");
    }
}