package com.toyproject.movie.core.repository.schedule.jdbc;

import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ScheduledSeatJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertScheduledSeat(List<ScheduledSeat> scheduledSeats) {
        String sql = "INSERT INTO scheduled_seat (seat_reservation_status, client_idx, schedule_idx, seat_idx) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                scheduledSeats,
                scheduledSeats.size(),
                (PreparedStatement ps, ScheduledSeat scheduledSeat) -> {
                    ps.setInt(1, scheduledSeat.getSeatReservationStatus().getValue());
                    ps.setObject(2, (scheduledSeat.getClientIdx()));
                    ps.setLong(3, scheduledSeat.getSchedule().getScheduleIdx());
                    ps.setLong(4, scheduledSeat.getSeat().getSeatIdx());
                });
    }
}
