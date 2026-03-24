package com.toyproject.movie.api.dto.theater.request;

import com.toyproject.movie.core.domain.theater.Theater;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import static com.toyproject.movie.common.constants.ValidationMessage.*;
import static com.toyproject.movie.common.constants.ValidationMessage.THEATER_NAME_NOT_BLANK;

@Schema(description = "상영관 등록 req")
public record TheaterCreateReq(
        @Schema(description = "영화관 지점명", example = "CGV 강남")
        @NotBlank(message = CINEMA_NAME_NOT_BLANK)
        String cinemaName,

        @Schema(description = "상영관 명칭", example = "1관")
        @NotBlank(message = THEATER_NAME_NOT_BLANK)
        String theaterName,

        @Schema(description = "마지막 좌석 행 (A-Z)", example = "J")
        @NotBlank(message = LAST_SEAT_ROW_NOT_BLANK)
        @Pattern(regexp = "^[A-Z]$", message = "행은 A부터 Z 사이의 대문자 한 글자여야 합니다.")
        String lastSeatRow,

        @Schema(description = "마지막 좌석 열 (번호)", example = "15")
        @NotNull(message = LAST_SEAT_COLUM_NOT_NULL)
        @Min(value = 1, message = LAST_SEAT_COLUM_MAX_MIN)
        @Max(value = 16, message = LAST_SEAT_COLUM_MAX_MIN)
        Integer lastSeatColum
) {
        public Theater toTheaterEntity(Integer seatCount) {
                return Theater.of(
                        this.cinemaName,
                        this.theaterName,
                        seatCount
                );
        }
}
