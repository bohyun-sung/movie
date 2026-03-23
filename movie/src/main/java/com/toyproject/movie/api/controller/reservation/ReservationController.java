package com.toyproject.movie.api.controller.reservation;

import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.common.response.Response;
import com.toyproject.movie.core.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public Response<Void> createReservation(@RequestBody @Valid ReservationCreateReq req) {
        reservationService.createReservation(req);
        return Response.success();
    }
}
