package com.toyproject.movie.core.repository.theater;

import com.toyproject.movie.core.domain.theater.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
