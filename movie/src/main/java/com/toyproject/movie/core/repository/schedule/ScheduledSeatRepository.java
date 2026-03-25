package com.toyproject.movie.core.repository.schedule;

import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduledSeatRepository extends JpaRepository<ScheduledSeat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ss from ScheduledSeat ss where ss.ssIdx in :ssIdxs")
    List<ScheduledSeat> findAllByIdWithLock(@Param("ssIdxs") List<Long> ssIdxs);
}
