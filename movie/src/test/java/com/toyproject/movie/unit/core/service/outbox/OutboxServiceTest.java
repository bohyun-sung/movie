package com.toyproject.movie.unit.core.service.outbox;

import com.toyproject.movie.api.dto.reservation.ReservationEventDto;
import com.toyproject.movie.core.domain.outbox.Outbox;
import com.toyproject.movie.core.repository.outbox.OutboxRepository;
import com.toyproject.movie.core.service.outbox.OutboxService;
import com.toyproject.movie.global.enums.DomainType;
import com.toyproject.movie.global.enums.EventNameType;
import com.toyproject.movie.support.fixture.reservation.ReservationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class OutboxServiceTest {

    @Mock
    private OutboxRepository outboxRepository;

    @InjectMocks
    private OutboxService outboxService;

    @Test
    @DisplayName("이벤트를 받으면 JSON으로 변환하여 Outbox 테이블에 저장한다")
    void saveEvent_Success() {

        // given
        ReservationEventDto dto = ReservationEventDto.from(ReservationFixture.defaultReservation());

        // when
        outboxService.saveEvent(DomainType.RESERVATION.name(), 1L, EventNameType.CREATED.name(), dto);

        // then
        // 실제로 save가 호출되었는지 확인
        ArgumentCaptor<Outbox> captor = ArgumentCaptor.forClass(Outbox.class);
        verify(outboxRepository).save(captor.capture());

        Outbox savedOutbox = captor.getValue();
        assertThat(savedOutbox.getAggregateType()).isEqualTo(DomainType.RESERVATION.name());
        assertThat(savedOutbox.getPayload()).contains("\"reservationStatusText\":\"결제 대기\""); // JSON 내용 검증
    }
}