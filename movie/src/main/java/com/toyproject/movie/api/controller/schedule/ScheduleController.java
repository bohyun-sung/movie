package com.toyproject.movie.api.controller.schedule;

import com.toyproject.movie.api.dto.schedule.request.ScheduleCreateReq;
import com.toyproject.movie.common.response.Response;
import com.toyproject.movie.core.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[00] 상영일정")
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "상영 일정 등록", description = "영화와 상영관을 매핑하여 새로운 상영 일정을 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상영 일정 등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패 (시작시간, 관람료 등)"),
            @ApiResponse(responseCode = "404", description = "영화 또는 상영관 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "해당 상영관에 이미 겹치는 상영 일정이 존재함")
    })
    @PostMapping
    public Response<Void> createSchedule(@RequestBody @Valid ScheduleCreateReq req) {
        scheduleService.createSchedule(req);
        return Response.success();
    }
}
