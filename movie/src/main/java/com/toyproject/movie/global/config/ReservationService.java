package com.toyproject.movie.global.config;

import com.toyproject.movie.core.domain.reservation.TempReservation;
import com.toyproject.movie.core.repository.reservation.TempReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final TempReservationRepository tempReservationRepository;
    private final StringRedisTemplate redisTemplate;

    public void occupySeat(Long movieId, String userId, String seatNumber) {
        String lockKey = "movie:" + movieId + ":seat:" + seatNumber;

        Boolean isAbsent = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, userId, Duration.ofMinutes(10));

        // 1. 이미 해당 좌석 키가 Redis에 존재하는지 확인
        if (!isAbsent) {
            throw new RuntimeException("이미 다른 고객이 점유");
        }

        // 2. 존재하지 않는다면 점유 정보 저장
        TempReservation temp = TempReservation.builder()
                .idx(lockKey)
                .movieIdx(movieId)
                .email(userId)
                .seatNumber(seatNumber)
                .reservationStatus("OCCUPIED")
                .build();

        tempReservationRepository.save(temp);
    }
}
