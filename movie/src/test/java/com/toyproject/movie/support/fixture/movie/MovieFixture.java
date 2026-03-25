package com.toyproject.movie.support.fixture.movie;

import com.toyproject.movie.core.domain.movie.Movie;
import com.toyproject.movie.global.enums.AudienceType;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovieFixture {
    /**
     * 영화 entity
     */
    public static Movie movieDefault() {
        return Movie.of(
                "재밋는 영화",
                LocalTime.of(2,30),
                AudienceType.FIFTEEN,
                0,
                LocalDate.of(2026,3,25),
                false
        );
    }
}
