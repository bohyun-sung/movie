package com.toyproject.movie.core.service.reservation;

import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.global.enums.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisCacheService {

    private final StringRedisTemplate redisTemplate;

    /**
     * 좌석 예매시 redis에 좌석이있는지 확인후 없으면 저장
     * @param scheduleIdx       상영관 idx
     * @param clientIdx         고객 idx
     * @param scheduleSeatIdx   상영관 좌석 idx
     */
    public String occupySeat(Long scheduleIdx, Long clientIdx, Long scheduleSeatIdx) {
        String lockKey = "schedule:" + scheduleIdx + ":seat:" + scheduleSeatIdx;
        String lockToken = String.valueOf(clientIdx);

        Boolean isAbsent = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockToken, Duration.ofMinutes(10));

        // 좌석 키가 Redis에 존재하는지 확인
        if (!isAbsent) {
            throw new ClientException(ExceptionType.BAD_REQUEST, "이미 다른 고객이 점유");
        }

        return lockKey;
    }

    /**
     * 좌석 점유 실패시 redis 락 해제
     * @param key       redisLockKey
     * @param clientIdx 고객 idx
     */
    public void rollBackSeat(String key, Long clientIdx) {
        String value = redisTemplate.opsForValue().get(key);
        if (String.valueOf(clientIdx).equals(value)) {
            redisTemplate.delete(key);
        }
    }
}
