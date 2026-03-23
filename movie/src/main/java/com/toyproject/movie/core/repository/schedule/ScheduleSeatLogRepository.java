package com.toyproject.movie.core.repository.schedule;

import com.toyproject.movie.core.domain.schedule.ScheduleSeatLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleSeatLogRepository extends JpaRepository<ScheduleSeatLog, Long> {
}
