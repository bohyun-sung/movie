package com.toyproject.movie.api.controller.reservation;

import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.common.response.Response;
import com.toyproject.movie.core.service.reservation.ReservationFacade;
import com.toyproject.movie.core.service.reservation.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[00] 예매")
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationFacade reservationFacade;

    @PostMapping
    public Response<Void> createReservation(@RequestBody @Valid ReservationCreateReq req) {
        reservationFacade.createReservationWithLock(req);
        return Response.success();
    }
}
