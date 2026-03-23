package com.toyproject.movie.api.dto.reservation;

import com.toyproject.movie.global.enums.AudienceDiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import static com.toyproject.movie.common.constants.ValidationMessage.AUDIENCE_NOT_NULL;
import static com.toyproject.movie.common.constants.ValidationMessage.SEAT_NOT_NULL;

public record ReservationSeatDto(
        @NotNull(message = SEAT_NOT_NULL)
        @Schema(description = "상영관 좌석 idx")
        Long ssIdx,
        @NotNull(message = AUDIENCE_NOT_NULL)
        @Schema(description = "연령별 할인 타입")
        AudienceDiscountType audienceDiscountType
) {
}
