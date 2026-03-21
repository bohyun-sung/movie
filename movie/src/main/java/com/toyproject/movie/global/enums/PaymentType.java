package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType implements BaseEnum {
    CARD(0, "카드"),
    PAY(1, "무통장 입금"),

    ;

    private final Integer value;
    private final String text;

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<PaymentType> {
        public Converter() {
            super(PaymentType.class);
        }
    }
}
