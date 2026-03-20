package com.toyproject.movie.core.repository.reservation;

import com.toyproject.movie.core.domain.reservation.TempReservation;
import org.springframework.data.repository.CrudRepository;

public interface TempReservationRepository extends CrudRepository<TempReservation, String> {
}
