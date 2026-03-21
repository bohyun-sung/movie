package com.toyproject.movie.core.domain.schedule;

import com.toyproject.movie.core.domain.base.DefaultTimeStampCreatedAndModifiedEntity;
import com.toyproject.movie.core.domain.theater.Seat;
import com.toyproject.movie.global.enums.SeatReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "scheduled_seat", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_schedule_seat_schedule_idx_seat_idx",
                columnNames = {"schedule_idx", "seat_idx"}
        )
})
@Entity
public class ScheduledSeat extends DefaultTimeStampCreatedAndModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ss_idx")
    private Long ssIdx;

    @Column(name = "seat_reservation_status", nullable = false,
            comment = "0: 예약가능, 1: 대기중, 2: 예약됨")
    @Convert(converter = SeatReservationStatus.Converter.class)
    private SeatReservationStatus seatReservationStatus;

    @Version
    private Long version;

    @Column(name = "client_idx", nullable = false)
    private Long clientIdx;

    @ManyToOne
    @JoinColumn(name = "schedule_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "seat_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Seat seat;


    private ScheduledSeat(SeatReservationStatus seatReservationStatus, Long clientIdx, Schedule schedule, Seat seat) {
        this.seatReservationStatus = seatReservationStatus;
        this.schedule = schedule;
        this.seat = seat;
        this.clientIdx = clientIdx;

    }

    public static ScheduledSeat of(SeatReservationStatus seatReservationStatus, Long clientIdx, Schedule schedule, Seat seat) {
        return new ScheduledSeat(seatReservationStatus, clientIdx, schedule, seat);
    }


}
