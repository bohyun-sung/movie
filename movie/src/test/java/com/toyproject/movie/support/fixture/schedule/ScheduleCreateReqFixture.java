package com.toyproject.movie.support.fixture.schedule;

import com.toyproject.movie.api.dto.schedule.request.ScheduleCreateReq;

import java.time.LocalDateTime;

public class ScheduleCreateReqFixture {
    public static ScheduleCreateReq createDefault() {
        return new ScheduleCreateReq(
                LocalDateTime.of(2026,6,25,10,00),
                15000,
                1L,
                1L);
    }
}
