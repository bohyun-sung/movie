package com.toyproject.movie.api.controller.theater;

import com.toyproject.movie.api.dto.theater.request.TheaterCreateReq;
import com.toyproject.movie.common.response.Response;
import com.toyproject.movie.core.service.thearter.TheaterService;
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

@Tag(name = "[00] 상영관")
@RequiredArgsConstructor
@RequestMapping("/api/v1/theaters")
@RestController
public class TheaterController {

    private final TheaterService theaterService;

    @Operation(summary = "상영관 및 좌석 등록", description = "영화관 지점명과 상영관 정보를 입력받아 상영관을 생성하고 좌석을 자동 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상영관 및 좌석 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패 (행/열 형식 등)"),
            @ApiResponse(responseCode = "409", description = "해당 지점에 이미 동일한 이름의 상영관이 존재함")
    })
    @PostMapping
    public Response<Void> createTheater(@RequestBody @Valid TheaterCreateReq req) {
        theaterService.createTheater(req);
        return Response.success();
    }
}
