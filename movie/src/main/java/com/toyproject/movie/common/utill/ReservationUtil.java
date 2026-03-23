package com.toyproject.movie.common.utill;

import com.toyproject.movie.global.enums.AudienceDiscountType;

public class ReservationUtil {

    public static Integer totalDiscount(Integer discountTicketPrice, AudienceDiscountType audienceDiscountType) {
        return (int) (discountTicketPrice * (1 - audienceDiscountType.getDiscount()));
    }


}
