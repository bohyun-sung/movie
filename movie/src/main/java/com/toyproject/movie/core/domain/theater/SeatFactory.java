package com.toyproject.movie.core.domain.theater;

import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class SeatFactory {
    /**
     * 입력받은 마지막 행/열 좌석으로 총좌석수 반환
     * @param lastSeatRow   마지막 행 ex) "D"
     * @param lastSeatColum 마지막 열 ex) 10
     * @return 총 좌석 수
     */
    public static int calculateTotalSeatsCount(String lastSeatRow, Integer lastSeatColum) {
        int rowCount = lastSeatRow.toUpperCase().charAt(0) - 'A' + 1;
        return rowCount * lastSeatColum;
    }

    /**
     * 상영관에 들어갈 좌석 만큼 총 seat 생성
     * @param lastSeatRow   마지막 행 ex) "D"
     * @param lastSeatColum 마지막 열 ex) 10
     * @param theater       상영관 entity
     * @return A~lastSeatRow * 1~lastSeatColum 의 seat 리스트
     */
    public static List<Seat> createSeats(String lastSeatRow, Integer lastSeatColum, Theater theater) {
        char endRow = lastSeatRow.toUpperCase(Locale.ROOT).charAt(0);

        return IntStream.rangeClosed('A', endRow)
                .mapToObj(row -> String.valueOf((char) row))
                .flatMap(row -> IntStream.rangeClosed(1, lastSeatColum)
                        .mapToObj(col -> Seat.of(row, col, theater)))
                .toList();
    }
}
