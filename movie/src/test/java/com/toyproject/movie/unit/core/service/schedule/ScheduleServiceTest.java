package com.toyproject.movie.unit.core.service.schedule;

import com.toyproject.movie.api.dto.schedule.request.ScheduleCreateReq;
import com.toyproject.movie.common.exception.ClientException;
import com.toyproject.movie.core.domain.movie.Movie;
import com.toyproject.movie.core.domain.schedule.Schedule;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.core.repository.movie.MovieRepository;
import com.toyproject.movie.core.repository.schedule.ScheduleRepository;
import com.toyproject.movie.core.repository.theater.TheaterRepository;
import com.toyproject.movie.core.service.schedule.ScheduleService;
import com.toyproject.movie.global.enums.ExceptionType;
import com.toyproject.movie.support.fixture.movie.MovieFixture;
import com.toyproject.movie.support.fixture.schedule.ScheduleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("상영 시간이 겹치지 않으면 상영 일정을 성공적으로 등록한다")
    void createSchedule_Success() {
        // Given
        ScheduleCreateReq req = ScheduleFixture.createScheduleCreateReqDefault();
        Movie movie = MovieFixture.movieDefault();
        Theater theater = Theater.of("판교","1관", 200);

        given(movieRepository.findById(req.movieIdx())).willReturn(Optional.of(movie));
        given(theaterRepository.existsById(req.theaterIdx())).willReturn(true);
        given(theaterRepository.getReferenceById(req.theaterIdx())).willReturn(theater);

        // 중요: 중복된 일정이 없다고 가정 (false)
        given(scheduleRepository.existsOverlappingSchedule(anyLong(), any(), any())).willReturn(false);

        // When
        scheduleService.createSchedule(req);

        // Then
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    @DisplayName("선택한 상영 시간에 이미 다른 일정이 있으면 Conflict 예외를 던진다")
    void createSchedule_Fail_Overlapping() {
        // Given
        ScheduleCreateReq req = ScheduleFixture.createScheduleCreateReqDefault();
        Movie movie = MovieFixture.movieDefault();

        given(movieRepository.findById(anyLong())).willReturn(Optional.of(movie));
        given(theaterRepository.existsById(anyLong())).willReturn(true);

        // 중요: 중복된 일정이 있다고 가정 (true)
        given(scheduleRepository.existsOverlappingSchedule(anyLong(), any(), any())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> scheduleService.createSchedule(req))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("이미 상영관에 상영일정이 등록 되어있습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 영화 IDX로 일정 등록 시 NOT_FOUND 예외가 발생한다")
    void createSchedule_Fail_MovieNotFound() {
        // Given
        ScheduleCreateReq req = ScheduleFixture.createScheduleCreateReqDefault();

        given(movieRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> scheduleService.createSchedule(req))
                .isInstanceOf(ClientException.class)
                .satisfies(ex -> {
                    ClientException clientEx = (ClientException) ex;
                    assertThat(clientEx.getType()).isEqualTo(ExceptionType.NOT_FOUND);
                });

    }
}
