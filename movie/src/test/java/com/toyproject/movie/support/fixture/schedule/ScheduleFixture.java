package com.toyproject.movie.support.fixture.schedule;

import com.toyproject.movie.api.dto.schedule.request.ScheduleCreateReq;
import com.toyproject.movie.core.domain.schedule.Schedule;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.global.enums.DiscountType;
import com.toyproject.movie.support.fixture.movie.MovieFixture;
import com.toyproject.movie.support.fixture.theater.TheaterFixture;

import java.time.LocalDateTime;

public class ScheduleFixture {
    /**
     * 상영일정 생성 REQ
     */
    public static ScheduleCreateReq createScheduleCreateReqDefault() {
        return new ScheduleCreateReq(
                LocalDateTime.of(2026,6,25,10,00),
                15000,
                1L,
                1L);
    }

    /**
     *  상영일정 entity
     */
    public static Schedule scheduleDefault() {
        return Schedule.of(
                LocalDateTime.of(2026,3,25,11,0),
                LocalDateTime.of(2026,3,25,12,30),
                15000,
                DiscountType.NORMAL,
                MovieFixture.movieDefault(),
                TheaterFixture.theaterDefault()



        );

    }
}
