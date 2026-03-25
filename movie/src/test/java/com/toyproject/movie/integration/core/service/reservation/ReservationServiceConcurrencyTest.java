package com.toyproject.movie.integration.core.service.reservation;

import com.toyproject.movie.api.dto.reservation.ReservationSeatDto;
import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.core.repository.reservation.ReservationDetailRepository;
import com.toyproject.movie.core.repository.reservation.ReservationRepository;
import com.toyproject.movie.core.repository.schedule.ScheduleSeatLogRepository;
import com.toyproject.movie.core.repository.schedule.ScheduledSeatRepository;
import com.toyproject.movie.core.service.reservation.ReservationFacade;
import com.toyproject.movie.global.enums.AudienceDiscountType;
import com.toyproject.movie.support.annotation.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestPropertySource(properties = {
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6380"
})
@IntegrationTest
class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ScheduledSeatRepository scheduledSeatRepository;
    @Autowired
    private ReservationFacade reservationFacade;
    @Autowired
    private ReservationDetailRepository reservationDetailRepository;
    @Autowired
    private ScheduleSeatLogRepository scheduleSeatLogRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        // Redis 초기화
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        // DB 초기화
        reservationDetailRepository.deleteAllInBatch();
        scheduleSeatLogRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        // 좌석 상태 원복
        ScheduledSeat seat = scheduledSeatRepository.findById(1L).orElseThrow();
        seat.initStatus();
        scheduledSeatRepository.saveAndFlush(seat);
    }

    @Test
    @DisplayName("100명이 동시에 한 좌석을 예매하면 한 명만 성공해야 한다")
    void concurrency_test_100_requests() throws InterruptedException {
        // Given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 100명이 준비될 때까지 대기
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // When
        for (int i = 0; i < threadCount; i++) {
            ReservationCreateReq req = createTestRequest((long) i + 1);
            executorService.submit(() -> {
                try {
                    reservationFacade.createReservationWithLock(req);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    log.error("예매 실패 사유: {}", e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        // 모든 스레드가 끝날 때까지 대기
        latch.await();

        // Then
        log.info("성공 횟수: {}", successCount.get());
        log.info("실패 횟수: {}", failCount.get());
        // 성공은 무조건 1명
        assertThat(successCount.get()).isEqualTo(1);
        // 나머지는 모두 실패
        assertThat(failCount.get()).isEqualTo(threadCount - 1);

        // DB 확인: 해당 스케줄 좌석의 예약 데이터가 1개인지 검증
        long reservationCount = reservationRepository.count();
        assertThat(reservationCount).isEqualTo(1);
    }

    private ReservationCreateReq createTestRequest(Long clientIdx) {
        ReservationSeatDto reservationSeatDto = new ReservationSeatDto(1L, AudienceDiscountType.ADULT);
        return new ReservationCreateReq(clientIdx, new ArrayList<>(Collections.singleton(reservationSeatDto)), 1L);
    }
}
