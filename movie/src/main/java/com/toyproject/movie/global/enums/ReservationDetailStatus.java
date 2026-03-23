package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationDetailStatus implements BaseEnum {
    PENDING(0, "예매 대기"),
    BOOKED(1, "예매 완료"),
    REFUNDED(2, "환불"),
    USED(3, "사용 완료"),
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
