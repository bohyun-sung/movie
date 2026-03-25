package com.toyproject.movie.unit.core.service.reservation;

import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.core.service.reservation.RedisCacheService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RedisCacheServiceTest {

    @InjectMocks
    private RedisCacheService redisCacheService;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Test
    @DisplayName("좌석이 비어있으면(Absent) Redis에 저장하고 락 키를 반환한다")
    void occupySeat_Success() {
        // Given
        Long scheduleIdx = 1L;
        Long clientIdx = 100L;
        Long seatIdx = 5L;
        String expectedKey = "schedule:1:seat:5";

        // RedisTemplate의 내부 동작 Mocking
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.setIfAbsent(anyString(), anyString(), any(Duration.class)))
                .willReturn(true); // 성공 상황 가정

        // When
        String resultKey = redisCacheService.occupySeat(scheduleIdx, clientIdx, seatIdx);

        // Then
        assertThat(resultKey).isEqualTo(expectedKey);
        verify(valueOperations).setIfAbsent(expectedKey, "100", Duration.ofMinutes(10));
    }

    @Test
    @DisplayName("이미 좌석이 점유되어 있으면 ClientException을 던진다")
    void occupySeat_Fail_Already_Occupied() {
        // Given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.setIfAbsent(anyString(), anyString(), any(Duration.class)))
                .willReturn(false); // 이미 데이터가 있는 상황 가정

        // When & Then
        assertThatThrownBy(() -> redisCacheService.occupySeat(1L, 100L, 5L))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("이미 다른 고객이 점유");
    }
}