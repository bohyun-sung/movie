package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatReservationStatus implements BaseEnum {
    AVAILABLE(0, "예약 가능"),
    PENDING(1, "대기중"),
    BOOKED(2, "예약됨"),
    ;

    private final Integer value;
    private final String text;

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<SeatReservationStatus> {
        public Converter() {
            super(SeatReservationStatus.class);
        }
    }
}
