package com.toyproject.movie.core.domain.theater;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seat", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_theater_seat_theater_idx_seat_column_seat_row",
                columnNames = {"theater_idx", "seat_column", "seat_row"}
        )
})
@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_idx", columnDefinition = "BIGINT UNSIGNED")
    private Long seatIdx;

    @Column(name = "seat_row", nullable = false, length = 10)
    private String seatRow;

    @Column(name = "seat_column", nullable = false)
    private Integer seatColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Theater theater;

    private Seat(String seatRow, Integer seatColumn, Theater theater) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.theater = theater;
    }

    public static Seat of(String seatRow, Integer seatColumn, Theater theater) {
        return new Seat(seatRow, seatColumn, theater);
    }

    public String getFullSeatNumber() {
        return seatRow + seatColumn;
    }
}
