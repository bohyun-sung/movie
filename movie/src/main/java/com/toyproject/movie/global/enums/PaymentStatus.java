package com.toyproject.movie.global.enums;

import com.toyproject.movie.global.enums.base.BaseEnum;
import com.toyproject.movie.global.enums.coverter.BaseEnumAttributeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus implements BaseEnum {
    READY(0, "결제 대기"),
    COMPLETED(1, "결제 완료"),
    CANCELLED(2, "결제 취소"),
    FAILED(3, "결제 실패")
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
