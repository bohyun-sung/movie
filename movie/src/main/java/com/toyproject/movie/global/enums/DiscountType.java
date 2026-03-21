package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType implements BaseEnum {
    NORMAL(0, "정상", 0),
    MORNING(1, "조조할인", 4000),
    ;

    private final Integer value;
    private final String text;
    private final Integer discount;

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<AudienceDiscountType> {
        public Converter() {
            super(AudienceDiscountType.class);
        }
    }
}
