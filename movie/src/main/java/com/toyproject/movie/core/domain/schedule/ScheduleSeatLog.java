package com.toyproject.movie.core.domain.schedule;

import com.toyproject.movie.core.domain.base.DefaultTimeStampCreatedEntity;
import com.toyproject.movie.global.enums.SeatReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seat_status_log")
@Entity
public class ScheduleSeatLog extends DefaultTimeStampCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ssl_idx")
    private Long sslIdx;

    @Column(name = "client_idx", nullable = false)
    private Long clientIdx;

    @Column(name = "seat_reservation_status", nullable = false,
            comment = "0: 예약가능, 1: 대기중, 2: 예약됨")
    @Convert(converter = SeatReservationStatus.Converter.class)
    private SeatReservationStatus seatReservationStatus;

    @Column(name = "reason", length = 100)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_ss_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ScheduledSeat seat;

    private ScheduleSeatLog(Long clientIdx, SeatReservationStatus seatReservationStatus, String reason, ScheduledSeat seat) {
        this.clientIdx = clientIdx;
        this.seatReservationStatus = seatReservationStatus;
        this.reason = reason;
        this.seat = seat;
    }

    public static ScheduleSeatLog of(Long clientIdx, SeatReservationStatus seatReservationStatus, String reason, ScheduledSeat seat) {
        return new ScheduleSeatLog(clientIdx, seatReservationStatus, reason, seat);
    }
}
