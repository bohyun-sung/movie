package com.toyproject.movie.api.dto.reservation;

import com.toyproject.movie.core.domain.reservation.Reservation;
import com.toyproject.movie.global.enums.ReservationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationEventDto {

    private Long reservationIdx;
    private ReservationStatus reservationStatus;
    private Integer reservationStatusValue;
    private String reservationStatusText;
    private Integer totalAmount;
    private String movieTitle;
    private String theaterName;
    private Long clientIdx;


    public static ReservationEventDto from(Reservation entity) {
        return new ReservationEventDto(
                entity.getReservationIdx(),
                entity.getReservationStatus(),
                entity.getReservationStatus().getValue(),
                entity.getReservationStatus().getText(),
                entity.getTotalAmount(),
                entity.getMovieTitle(),
                entity.getTheaterName(),
                entity.getClient().getClientIdx()
        );
    }
}
