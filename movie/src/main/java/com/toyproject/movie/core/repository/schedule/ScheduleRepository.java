package com.toyproject.movie.core.repository.schedule;

import com.toyproject.movie.core.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
