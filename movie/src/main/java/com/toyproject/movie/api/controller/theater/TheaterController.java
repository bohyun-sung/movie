package com.toyproject.movie.api.controller.theater;

import com.toyproject.movie.api.dto.theater.request.TheaterCreateReq;
import com.toyproject.movie.common.response.Response;
import com.toyproject.movie.core.service.thearter.TheaterService;
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

    @PostMapping
    public Response<Void> createTheater(@RequestBody @Valid TheaterCreateReq req) {
        theaterService.createTheater(req);
        return Response.success();
    }
}
