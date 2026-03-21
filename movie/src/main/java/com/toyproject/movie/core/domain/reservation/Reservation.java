package com.toyproject.movie.core.domain.reservation;

import com.toyproject.movie.core.domain.base.DefaultTimeStampCreatedAndModifiedEntity;
import com.toyproject.movie.core.domain.client.Client;
import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.global.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservation")
@Entity
public class Reservation extends DefaultTimeStampCreatedAndModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_idx", columnDefinition = "BIGINT UNSIGNED")
    private Long reservation_idx;

    @Column(name = "reservation_status", nullable = false,
            comment = "0: 결제 대기, 1: 예매 완료, 2: 부분 환불, 3: 환불")
    @Convert(converter = ReservationStatus.Converter.class)
    private ReservationStatus reservationStatus;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "movie_title", nullable = false, length = 50)
    private String movieTitle;

    @Column(name = "theater_name", nullable = false, length = 30)
    private String theaterName;

    @Column(name = "reservation_number", nullable = false, length = 50)
    private String reservationNumber;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ss_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ScheduledSeat scheduledSeat;

    private Reservation(ReservationStatus reservationStatus, Integer totalAmount, String movieTitle, String theaterName, String reservationNumber, LocalDateTime expiredAt, Client client, ScheduledSeat scheduledSeat) {
        this.reservationStatus = reservationStatus;
        this.totalAmount = totalAmount;
        this.movieTitle = movieTitle;
        this.theaterName = theaterName;
        this.reservationNumber = reservationNumber;
        this.expiredAt = expiredAt;
        this.client = client;
        this.scheduledSeat = scheduledSeat;
    }

    public static Reservation of(ReservationStatus reservationStatus, Integer totalAmount, String movieTitle, String theaterName, String reservationNumber, LocalDateTime expiredAt, Client client, ScheduledSeat scheduledSeat) {
        return new Reservation(reservationStatus, totalAmount, movieTitle, theaterName, reservationNumber, expiredAt, client, scheduledSeat);
    }
}
