package com.toyproject.movie.support.fixture.reservation;

import com.toyproject.movie.api.dto.reservation.ReservationSeatDto;
import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.global.enums.AudienceDiscountType;

import java.util.List;

public class ReservationFixture {
    /**
     * 영화 예매
     */
    public static ReservationCreateReq defaultCreateReservation() {
        ReservationSeatDto dto = new ReservationSeatDto(1L, AudienceDiscountType.ADULT);
        List<ReservationSeatDto> dtos = List.of(dto);
        return new ReservationCreateReq(
                1L,
                dtos,
                1L
        );
    }
}
