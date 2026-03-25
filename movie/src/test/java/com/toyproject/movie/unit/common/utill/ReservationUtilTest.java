package com.toyproject.movie.unit.common.utill;

import com.toyproject.movie.common.utill.ReservationUtil;
import com.toyproject.movie.global.enums.AudienceDiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
class ReservationUtilTest {

    @ParameterizedTest
    @CsvSource({
            "15000, ADULT, 0, 15000",
            "15000, TEENAGER, 0, 12000",
            "15000, CHILD, 0, 7500",
            "15000, ADULT, 4000, 11000", // 조조(4000원 할인) 적용 케이스
            "15000, TEENAGER, 4000, 8800",
            "15000, CHILD, 4000, 5500"
    })
    @DisplayName("연령별 할인과 조조 할인이 복합적으로 적용되어도 정확한 금액을 계산")
    void totalDiscount_Complex_Success(int price, AudienceDiscountType type, int morningDiscount, int expected) {
        Integer result = ReservationUtil.totalDiscount(price - morningDiscount, type);
        assertThat(result).isEqualTo(expected);
    }
}