package com.toyproject.movie.unit.core.service.theater;

import com.toyproject.movie.api.dto.theater.request.TheaterCreateReq;
import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.core.domain.theater.Seat;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.core.repository.theater.TheaterRepository;
import com.toyproject.movie.core.repository.theater.jdbc.SeatJdbcRepository;
import com.toyproject.movie.core.service.thearter.TheaterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TheaterServiceTest {

    @InjectMocks
    private TheaterService theaterService;

    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private SeatJdbcRepository seatJdbcRepository;

    @Test
    @DisplayName("상영관 생성 시 입력된 행/열에 맞춰 좌석 리스트 생성")
    void createTheater_Logic_Test() {
        // Given
        TheaterCreateReq req = new TheaterCreateReq("판교", "1관", "J", 20);

        BDDMockito.given(theaterRepository.existsByCinemaNameAndTheaterName(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .willReturn(false);

        Theater mockTheater = Theater.of("판교", "1", 200);
        BDDMockito.given(theaterRepository.save(ArgumentMatchers.any(Theater.class)))
                .willReturn(mockTheater);

        // When
        theaterService.createTheater(req);

        // Then
        // 중복 체크 호출되었는지
        Mockito.verify(theaterRepository, Mockito.times(1)).existsByCinemaNameAndTheaterName("판교", "1관");
        ArgumentCaptor<List<Seat>> seatListCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(seatJdbcRepository).batchInsertSeats(seatListCaptor.capture());

        List<Seat> capturedSeats = seatListCaptor.getValue();
        assertThat(capturedSeats).hasSize(200);

    }

    @Test
    @DisplayName("상영관 이름이 중복되면 예외가 발생한다")
    void createTheater_Duplicate_Exception() {
        // Given
        TheaterCreateReq req = new TheaterCreateReq("판교", "1관", "A", 10);
        BDDMockito.given(theaterRepository.existsByCinemaNameAndTheaterName("판교", "1관"))
                .willReturn(true);

        // When & Then
        assertThatThrownBy(() -> theaterService.createTheater(req))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("이미 해당 영화관에 동일한 이름의 상영관이 존재합니다.");
    }
}
