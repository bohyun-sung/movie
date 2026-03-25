package com.toyproject.movie.core.repository.theater;

import com.toyproject.movie.core.domain.theater.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s " +
            "WHERE s.theater.theaterIdx = :theaterIdx")
    List<Seat> findAllByTheaterIdx(Long theaterIdx);
}
