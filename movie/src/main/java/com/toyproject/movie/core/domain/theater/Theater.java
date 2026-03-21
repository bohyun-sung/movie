package com.toyproject.movie.core.domain.theater;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "theater")
@Entity
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_idx")
    private Long theaterIdx;

    @Column(name = "cinema_name", nullable = false, length = 30)
    private String cinemaName;

    @Column(name = "theater_name", nullable = false, length = 30)
    private String theaterName;

    @Column(name = "seat_count", nullable = false)
    private Integer seatCount;


    private Theater(String cinemaName, String theaterName, Integer seatCount) {
        this.cinemaName = cinemaName;
        this.theaterName = theaterName;
        this.seatCount = seatCount;
    }

    public static Theater of(String cinemaName, String theaterName, Integer seatCount) {
        return new Theater(cinemaName, theaterName, seatCount);
    }
}
