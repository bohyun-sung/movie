package com.toyproject.movie.support.fixture.theater;

import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.global.enums.SeatReservationStatus;
import com.toyproject.movie.support.fixture.schedule.ScheduleFixture;

public class ScheduledSeatFixture {

    public static ScheduledSeat scheduledSeatDefault(SeatReservationStatus status) {
        return ScheduledSeat.of(
                status,
                1L,
                ScheduleFixture.scheduleDefault(),
                SeatFixture.seatDefault()

        );
    }
}
