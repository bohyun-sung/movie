package com.toyproject.movie.core.service.reservation;

import com.toyproject.movie.api.dto.reservation.ReservationSeatDto;
import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.core.repository.reservation.ReservationRepository;
import com.toyproject.movie.core.repository.schedule.ScheduledSeatRepository;
import com.toyproject.movie.global.enums.AudienceDiscountType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6380"
})
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ScheduledSeatRepository scheduledSeatRepository;

    @AfterEach
    void cleanUp() {
        // 테스트 후 데이터 정리 로직 필요
    }
    @Test
    @DisplayName("100명이 동시에 한 좌석을 예매하면 한 명만 성공해야 한다")
    void concurrency_test_100_requests() throws InterruptedException {
        // Given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 100명이 준비될 때까지 대기

        ReservationCreateReq req = createTestRequest(); // 테스트용 DTO 생성 (동일 좌석)

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.createReservation(req);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    log.error("예매 실패 사유: {}", e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기

        // Then
        log.info("성공 횟수: {}", successCount.get());
        log.info("실패 횟수: {}", failCount.get());

        assertThat(successCount.get()).isEqualTo(1); // 성공은 무조건 1명
        assertThat(failCount.get()).isEqualTo(threadCount - 1); // 나머지는 모두 실패

        // DB 확인: 해당 스케줄 좌석의 예약 데이터가 1개인지 검증
        long reservationCount = reservationRepository.count();
        assertThat(reservationCount).isEqualTo(1);
    }

    private ReservationCreateReq createTestRequest() {
        ReservationSeatDto reservationSeatDto = new ReservationSeatDto(1L, AudienceDiscountType.ADULT);
        return new ReservationCreateReq(1L,new ArrayList<>(Collections.singleton(reservationSeatDto)),1L);
    }
}
