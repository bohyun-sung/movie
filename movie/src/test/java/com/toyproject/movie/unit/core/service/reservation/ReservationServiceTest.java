package com.toyproject.movie.unit.core.service.reservation;

import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.core.repository.schedule.ScheduledSeatRepository;
import com.toyproject.movie.core.service.reservation.RedisCacheService;
import com.toyproject.movie.core.service.reservation.ReservationService;
import com.toyproject.movie.global.enums.SeatReservationStatus;
import com.toyproject.movie.support.fixture.reservation.ReservationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.toyproject.movie.support.fixture.theater.ScheduledSeatFixture.scheduledSeatDefault;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private RedisCacheService redisCacheService;
    @Mock
    private ScheduledSeatRepository scheduledSeatRepository;


    @Test
    @DisplayName("이미 예약된 좌석일 경우 예외가 발생하고 Redis 점유 해체")
    void createReservation_Fail_AlreadyReserved() {
        // Given
        ReservationCreateReq req = ReservationFixture.defaultCreateReservation();
        ScheduledSeat reservedSeat = scheduledSeatDefault(SeatReservationStatus.PENDING);

        given(redisCacheService.occupySeat(any(), any(), any())).willReturn("dummy-key");
        given(scheduledSeatRepository.findAllById(any())).willReturn(List.of(reservedSeat));

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(req))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("이미 예약된 좌석입니다.");

        // Redis 롤백
        Mockito.verify(redisCacheService, times(1)).rollBackSeats(any());
    }


}
