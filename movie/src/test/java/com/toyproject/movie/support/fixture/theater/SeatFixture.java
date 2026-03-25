package com.toyproject.movie.support.fixture.theater;

import com.toyproject.movie.core.domain.theater.Seat;

public class SeatFixture {

    public static Seat seatDefault() {
        return Seat.of("A", 1, TheaterFixture.theaterDefault());
    }
}
