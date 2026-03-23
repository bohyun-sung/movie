package com.toyproject.movie.core.repository.reservation;

import com.toyproject.movie.core.domain.reservation.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {
}
