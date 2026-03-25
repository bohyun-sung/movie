package com.toyproject.movie.unit.core.service.reservation;

import com.toyproject.movie.api.dto.reservation.request.ReservationCreateReq;
import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.core.repository.schedule.ScheduledSeatRepository;
import com.toyproject.movie.core.service.reservation.ReservationService;
import com.toyproject.movie.global.enums.SeatReservationStatus;
import com.toyproject.movie.support.fixture.reservation.ReservationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.toyproject.movie.support.fixture.theater.ScheduledSeatFixture.scheduledSeatDefault;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ScheduledSeatRepository scheduledSeatRepository;


    @Test
    @DisplayName("이미 예약된 좌석(PENDING)일 경우 ClientException 발생")
    void createReservation_Fail_AlreadyReserved() {
        // Given
        Long ssIdx = 1L;
        ReservationCreateReq req = ReservationFixture.defaultCreateReservation(ssIdx);
        // 좌석이 이미 PENDING 상태라고 가정
        ScheduledSeat reservedSeat = scheduledSeatDefault(SeatReservationStatus.PENDING);
        // Mock 객체의 PK 필드에 강제로 주입
        ReflectionTestUtils.setField(reservedSeat, "ssIdx", ssIdx);

        given(scheduledSeatRepository.findAllByIdWithLock(any())).willReturn(List.of(reservedSeat));

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(req))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("이미 예약된 좌석입니다.");
    }


}
