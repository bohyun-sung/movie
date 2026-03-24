package com.toyproject.movie.core.repository.schedule;

import com.toyproject.movie.core.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT COUNT(s) > 0 FROM Schedule s " +
            "WHERE s.theater.theaterIdx = :theaterIdx " +
            "AND s.startAt < :endAt " +
            "AND s.endAt > :startAt")
    boolean existsOverlappingSchedule(Long theaterIdx, LocalDateTime startAt, LocalDateTime endAt);
}
