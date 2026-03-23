package com.toyproject.movie.api.dto.reservation;

import com.toyproject.movie.core.domain.client.Client;
import com.toyproject.movie.global.enums.ReservationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationDto {
    private final Long reservation_idx;
    private final ReservationStatus reservationStatus;
    private final Integer totalAmount;
    private final String movieTitle;
    private final String theaterName;
    private final String reservationNumber;
    private final LocalDateTime expiredAt;
    private final Client client;


}
