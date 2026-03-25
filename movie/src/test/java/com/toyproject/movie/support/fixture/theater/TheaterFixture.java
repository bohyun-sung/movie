package com.toyproject.movie.support.fixture.theater;

import com.toyproject.movie.core.domain.theater.Theater;

public class TheaterFixture {

    /**
     * 상영관 entity
     */
    public static Theater theaterDefault() {
        return Theater.of(
                "판교",
                "1관",
                100
        );
    }
}
