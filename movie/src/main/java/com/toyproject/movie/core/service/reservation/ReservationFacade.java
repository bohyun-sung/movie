package com.toyproject.movie.core.service.reservation;

import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReservationFacade {

    private final ReservationService reservationService;
    private final RedisCacheService redisCacheService;

    /**
     * Redis 락 + 트랜잭션 제어 를 위해 퍼사이드 패턴 사용
     */
    public void createReservationWithLock(ReservationCreateReq req) {
        List<String> occupiedKeys = new ArrayList<>();
        try {
            // 트랜잭션 시작 전 Redis 락(점유) 먼저 획득
            req.reservationSeatDtos().forEach(dto -> {
                String occupiedKey = redisCacheService.occupySeat(
                        req.scheduleIdx(), req.clientIdx(), dto.ssIdx());
                occupiedKeys.add(occupiedKey);
            });

            // 실제 DB 작업 (트랜잭션) 수행
            // 이 메서드가 리턴될 때 DB 커밋이 완료됩니다.
            reservationService.createReservation(req);

        } catch (Exception e) {
            log.error("예매 실패로 인한 redis 롤백 수행, 사유: {}", e.getMessage());
            if (!occupiedKeys.isEmpty()) {
                occupiedKeys.forEach(key -> redisCacheService.rollBackSeat(key, req.clientIdx()));
            }
            throw e;
        }
    }
}