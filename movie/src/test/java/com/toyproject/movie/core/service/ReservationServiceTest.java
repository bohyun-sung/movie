package com.toyproject.movie.core.service;

import com.toyproject.movie.global.config.ReservationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6380"
})
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("이미 점유된 좌석을 예약하려 하면 RuntimeException 발생한다")
    void occupySeat_ThrowsException_IfAlreadyOccupied() {


        assertThatThrownBy(() -> reservationService.occupySeat(1L, "userA", "A1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 다른 고");
    }

    @Test
    @DisplayName("동시에 10명이 같은 좌석을 점유하려고 시도해도 단 1명만 성공해야 한다")
    void occupySeat_ConcurrencyTest() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        Long movieId = 1L;
        String seat = "C10";

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    reservationService.occupySeat(movieId, "user" + Math.random(), seat);
                    successCount.getAndIncrement();
                } catch (RuntimeException e) {
                    failCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        assertThat(successCount.get()).isEqualTo(1); // 성공은 무조건 1명
        assertThat(failCount.get()).isEqualTo(threadCount - 1); // 나머지는 모두 실패
    }
}