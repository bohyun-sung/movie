package com.toyproject.movie.api.dto.schedule.request;

import com.toyproject.movie.core.domain.movie.Movie;
import com.toyproject.movie.core.domain.schedule.Schedule;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.global.enums.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.toyproject.movie.common.constants.ValidationMessage.*;

@Schema(description = "상영일정 등록 REQ")
public record ScheduleCreateReq(
        @Schema(description = "상영 시작 시간", example = "2024-03-25T14:30:00")
        @NotNull(message = START_AT_NOT_NULL)
        @Future(message = START_AT_FUTURE)
        LocalDateTime startAt,

        @Schema(description = "영화 관람료 (원)", example = "15000")
        @NotNull(message = TICKET_PRICE_NOT_NULL)
        @PositiveOrZero(message = TICKET_PRICE_POSITIVE_OR_ZERO)
        Integer ticketPrice,

        @Schema(description = "영화 idx", example = "1")
        @NotNull(message = MOVIE_IDX_NOT_NULL)
        Long movieIdx,

        @Schema(description = "상영관 idx", example = "1")
        @NotNull(message = THEATER_IDX_NOT_NULL)
        Long theaterIdx
) {
        public Schedule toEntity(Movie movie, Theater referenceTheater) {

                DiscountType discountType = DiscountType.determineByStartAt(this.startAt());

                // 영화 종료 시간
                LocalTime runtime = movie.getRuntime();
                LocalDateTime endAt = this.startAt()
                        .plusHours(runtime.getHour())
                        .plusMinutes(runtime.getMinute())
                        .plusSeconds(runtime.getSecond());
                return Schedule.of(
                        this.startAt,
                        endAt,
                        this.ticketPrice,
                        discountType,
                        movie,
                        referenceTheater
                );
        }
}
