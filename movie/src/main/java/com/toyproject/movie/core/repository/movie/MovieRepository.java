package com.toyproject.movie.core.repository.movie;

import com.toyproject.movie.core.domain.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
