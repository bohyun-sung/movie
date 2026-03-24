package com.toyproject.movie.core.service.movie;

import com.toyproject.movie.api.dto.movie.request.MovieCreateReq;
import com.toyproject.movie.core.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional
    public void createMovie(MovieCreateReq req) {
        movieRepository.save(req.toEntity());
    }
}
