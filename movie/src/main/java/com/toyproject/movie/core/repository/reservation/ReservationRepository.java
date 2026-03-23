package com.toyproject.movie.core.repository.reservation;

import com.toyproject.movie.core.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
