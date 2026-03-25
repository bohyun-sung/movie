package com.toyproject.movie.unit.core.domain.theater;

import com.toyproject.movie.core.domain.theater.Seat;
import com.toyproject.movie.core.domain.theater.SeatFactory;
import com.toyproject.movie.core.domain.theater.Theater;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SeatFactoryTest {

    @Test
    @DisplayName("마지막 행이 'F' 이고 마지막 열이 20 이면 총 좌석수는 120개여야 한다")
    void calculateTotalSeatsCount_Success() {
        // Given
        String lastRow = "F";
        int lastCol = 20;
        // When
        int result = SeatFactory.calculateTotalSeatsCount(lastRow, lastCol);
        // Then
        Assertions.assertThat(result).isEqualTo(120);
    }

    @Test
    @DisplayName("A부터 B행 1~2열까지 Seat 객체가 총 4개 Seat 엔티티가 생성 되어야한다")
    void createSeats_Success() {
        // Given
        Theater mockTheater = Theater.of("판교", "1관",4);
        // When
        List<Seat> seats = SeatFactory.createSeats("B", 2, mockTheater);
        // Then
        Assertions.assertThat(seats).hasSize(4);
        Assertions.assertThat(seats.get(0).getSeatRow()).isEqualTo("A");
        Assertions.assertThat(seats.get(0).getSeatColumn()).isEqualTo(1);
        Assertions.assertThat(seats.get(2).getSeatRow()).isEqualTo("B");
        Assertions.assertThat(seats.get(2).getSeatColumn()).isEqualTo(1);
        Assertions.assertThat(seats.get(3).getSeatRow()).isEqualTo("B");
        Assertions.assertThat(seats.get(3).getSeatColumn()).isEqualTo(2);
        Assertions.assertThat(seats.get(0).getTheater()).isEqualTo(mockTheater);
    }
}