package com.toyproject.movie.core.domain.reservation;

import com.toyproject.movie.core.domain.base.DefaultTimeStampCreatedEntity;
import com.toyproject.movie.core.domain.schedule.ScheduledSeat;
import com.toyproject.movie.global.enums.AudienceDiscountType;
import com.toyproject.movie.global.enums.ReservationDetailStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservation_detail")
@Entity
public class ReservationDetail extends DefaultTimeStampCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rdIdx;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "audience_discount_type", nullable = false,
            comment = "0: 성인, 1: 청소년, 2: 유아")
    @Convert(converter = AudienceDiscountType.Converter.class)
    private AudienceDiscountType audienceDiscountType;

    @Column(name = "좌석명", nullable = false, length = 10)
    private String seatName;

    @Column(name = "reservation_deltail_status", nullable = false,
            comment = "0: 예매 대기, 1: 예매 완료, 2: 환불, 3: 사용 완료")
    @Convert(converter = ReservationDetailStatus.Converter.class)
    private ReservationDetailStatus reservationDetailStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ss_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ScheduledSeat scheduledSeat;

    private ReservationDetail(Integer price, AudienceDiscountType audienceDiscountType, String seatName, ReservationDetailStatus reservationDetailStatus, Reservation reservation, ScheduledSeat scheduledSeat) {
        this.price = price;
        this.audienceDiscountType = audienceDiscountType;
        this.seatName = seatName;
        this.reservationDetailStatus = reservationDetailStatus;
        this.reservation = reservation;
        this.scheduledSeat = scheduledSeat;
    }

    public static ReservationDetail of(Integer price, AudienceDiscountType audienceDiscountType, String seatName, ReservationDetailStatus reservationDetailStatus, Reservation reservation, ScheduledSeat scheduledSeat) {
        return new ReservationDetail(price, audienceDiscountType, seatName, reservationDetailStatus, reservation, scheduledSeat);
    }
}
