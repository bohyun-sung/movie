package com.toyproject.movie.api.dto.movie.request;

import com.toyproject.movie.core.domain.movie.Movie;
import com.toyproject.movie.global.enums.AudienceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.toyproject.movie.common.constants.ValidationMessage.*;

@Schema(name = "영화 등록 REQ")
public record MovieCreateReq(
        @Schema(description = "영화 제목", example = "인셉션")
        @NotBlank(message = MOVIE_TITLE_NOT_BLANK)
        String movieTitle,

        @Schema(description = "상영 시간", example = "02:28:00")
        @NotNull(message = RUNTIME_NOT_NULL)
        LocalTime runtime,

        @Schema(description = "관람 등급 (ALL, TWELVE, FIFTEEN, EIGHTEEN)", example = "ALL")
        @NotNull(message = AUDIENCE_NOT_NULL)
        AudienceType audienceType,

        @Schema(description = "개봉일", example = "2024-03-24")
        @NotNull(message = RELEASE_DATE_NOT_NULL)
        LocalDate releaseDate
) {
    public Movie toEntity() {
        return Movie.of(
                this.movieTitle,
                this.runtime,
                this.audienceType,
                0,
                this.releaseDate,
                false
        );
    }
}
