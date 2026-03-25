package com.toyproject.movie.core.repository.theater.jdbc;

import com.toyproject.movie.core.domain.theater.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class SeatJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertSeats(List<Seat> seats) {
        String sql = "INSERT INTO seat (seat_row, seat_column, theater_idx) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                seats,
                seats.size(),
                (PreparedStatement ps, Seat seat) -> {
                    ps.setString(1, seat.getSeatRow());
                    ps.setInt(2, seat.getSeatColumn());
                    ps.setLong(3, seat.getTheater().getTheaterIdx());
                });
    }
}
