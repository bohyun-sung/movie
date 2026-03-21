package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus implements BaseEnum {
    PAYMENT_PENDING(0, "결제 대기"),
    CONFIRMED(1, "예매 완료"),
    PARTIALLY_REFUNDED(2, "부분 환불"),
    REFUNDED(3, "환불"),
    ;

    private final Integer value;
    private final String text;

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<ReservationStatus> {
        public Converter() {
            super(ReservationStatus.class);
        }
    }
}
