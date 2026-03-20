package com.toyproject.movie.core.domain.reservation;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@RedisHash(value = "temp_reservation", timeToLive = 600)
public class TempReservation {
    @Id
    private String idx;

    @Indexed
    private Long movieIdx;

    private String email;
    private String seatNumber;

    private String reservationStatus;
}
