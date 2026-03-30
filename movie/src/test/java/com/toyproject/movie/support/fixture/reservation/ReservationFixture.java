package com.toyproject.movie.support.fixture.reservation;

import com.toyproject.movie.api.dto.reservation.ReservationSeatDto;
import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.core.domain.client.Client;
import com.toyproject.movie.core.domain.reservation.Reservation;
import com.toyproject.movie.global.enums.AudienceDiscountType;
import com.toyproject.movie.global.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationFixture {

    public static Reservation defaultReservation() {
        return Reservation.of(
                ReservationStatus.PAYMENT_PENDING,
                15000,
                "재밋는영화",
                "1관",
                "테스트 예매번호",
                LocalDateTime.now().plusMinutes(10),
                Client.of("test@test.com", "123", "010-0000-0001", false)
        );
    }
    /**
     * 영화 예매
     */
    public static ReservationCreateReq defaultCreateReservation(Long ssIdx) {
        ReservationSeatDto dto = new ReservationSeatDto(ssIdx, AudienceDiscountType.ADULT);
        List<ReservationSeatDto> dtos = List.of(dto);
        return new ReservationCreateReq(
                1L,
                dtos,
                1L
        );
    }
}
