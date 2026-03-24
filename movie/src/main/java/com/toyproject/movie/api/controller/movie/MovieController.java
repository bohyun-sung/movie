package com.toyproject.movie.api.controller.movie;

import com.toyproject.movie.api.dto.movie.request.MovieCreateReq;
import com.toyproject.movie.common.response.Response;
import com.toyproject.movie.core.service.movie.MovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[00] 영화")
@RequiredArgsConstructor
@RequestMapping("/api/v1/Movies")
@RestController
public class MovieController {
    private final MovieService movieService;
    @PostMapping
    public Response<Void> createMovie(@RequestBody @Valid MovieCreateReq req) {
        movieService.createMovie(req);
        return Response.success();
    }
}
