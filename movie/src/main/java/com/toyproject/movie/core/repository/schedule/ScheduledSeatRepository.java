package com.toyproject.movie.core.repository.schedule;

import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledSeatRepository extends JpaRepository<ScheduledSeat, Long> {
}
