package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationDetailStatus implements BaseEnum {
    BOOKED(0, "예매 완료"),
    REFUNDED(1, "환불"),
    USED(2, "사용 완료"),
    ;

    private final Integer value;
    private final String text;

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<ReservationDetailStatus> {
        public Converter() {
            super(ReservationDetailStatus.class);
        }
    }
}
