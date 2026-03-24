package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public enum DiscountType implements BaseEnum {
    NORMAL(0, "정상", 0),
    MORNING(1, "조조할인", 4000),
    ;

    private final Integer value;
    private final String text;
    private final Integer discount;

    /**
     *  조조 할인 인지 구분 10시 이전 조조할인
     * @param startAt 영화 상영시간
     * @return DiscountType
     */
    public static DiscountType determineByStartAt(LocalDateTime startAt) {
        return startAt.toLocalTime()
                .isAfter(LocalTime.of(10,0,0))
                ? DiscountType.NORMAL
                : DiscountType.MORNING;
    }

    @jakarta.persistence.Converter
    public static class Converter extends BaseEnumAttributeConverter<AudienceDiscountType> {
        public Converter() {
            super(AudienceDiscountType.class);
        }
    }

}
