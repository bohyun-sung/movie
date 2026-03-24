package com.toyproject.movie.core.repository.theater;

import com.toyproject.movie.core.domain.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    boolean existsByCinemaNameAndTheaterName(String cinemaName, String theaterName);
}
