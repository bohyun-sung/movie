package com.toyproject.movie.api.dto.reservation.request;

import com.toyproject.movie.api.dto.reservation.ReservationSeatDto;
import com.toyproject.movie.core.domain.client.Client;
import com.toyproject.movie.core.domain.reservation.Reservation;
import com.toyproject.movie.global.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import static com.toyproject.movie.common.constants.ValidationMessage.*;

@Schema(description = "영화 예매 REQ")
public record ReservationCreateReq(
        @NotNull(message = CLIENT_NOT_NULL)
        @Schema(description = "고객 idx", example = "1")
        Long clientIdx,

        @NotEmpty(message = SEAT_NOT_EMPTY)
        List<ReservationSeatDto> reservationSeatDtos,

        @NotNull(message = SCHEDULE_NOT_NULL)
        @Schema(description = "상영일정 idx")
        Long scheduleIdx
) {
    public Reservation toReservationEntity(Integer discountTicketPrice, String movieTitle, String cinemaTheaterName, String reservationNumber, Client client) {
        return Reservation.of(
                ReservationStatus.PAYMENT_PENDING,
                discountTicketPrice,
                movieTitle,
                cinemaTheaterName,
                reservationNumber,
                LocalDateTime.now().plusMinutes(10),
                client
        );
    }
}
