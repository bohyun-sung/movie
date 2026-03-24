package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AudienceType implements BaseEnum {
    ALL(0, "전체 관람가"),
    TWELVE(12, "12세이상관람가"),
    FIFTEEN(15, "15세이상관람가"),
    EIGHTEEN(18, "18세이상관람가"),
    ;

    private final Integer value;
    private final String text;

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<AudienceType> {
        public Converter() {
            super(AudienceType.class);
        }
    }
}
