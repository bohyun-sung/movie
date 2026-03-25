package com.toyproject.movie.common.utill;

import com.toyproject.movie.global.enums.AudienceDiscountType;

public class ReservationUtil {

    /**
     * 할인된 티켓에 관람객의 연령에 따라 추가 할인 적용
     * @param discountTicketPrice  할인된 티켓가격  [현재 조조 할인 만 존재]
     * @param audienceDiscountType 관람객 연령
     */
    public static Integer totalDiscount(Integer discountTicketPrice, AudienceDiscountType audienceDiscountType) {
        return (int) Math.round(discountTicketPrice * (1 - audienceDiscountType.getDiscount()));
    }

}
