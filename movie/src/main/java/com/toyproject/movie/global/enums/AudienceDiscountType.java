package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AudienceDiscountType implements BaseEnum {
    ADULT(0, "성인", 0.0),
    TEENAGER(1, "청소년", 0.2),
    CHILD(2, "유아", 0.5),
    ;

    private final Integer value;
    private final String text;
    private final Double discount;

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<AudienceDiscountType> {
        public Converter() {
            super(AudienceDiscountType.class);
        }
    }
}
