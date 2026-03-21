package com.toyproject.movie.core.domain.payment;

import com.toyproject.movie.core.domain.base.DefaultTimeStampCreatedEntity;
import com.toyproject.movie.core.domain.reservation.Reservation;
import com.toyproject.movie.global.enums.PaymentStatus;
import com.toyproject.movie.global.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_log")
@Entity
public class PaymentLog extends DefaultTimeStampCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pl_idx")
    private Long plIdx;

    @Column(name = "payment_status", nullable = false)
    @Convert(converter = PaymentStatus.Converter.class)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_type", nullable = false)
    @Convert(converter = PaymentType.Converter.class)
    private PaymentType paymentType;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "pg_id", nullable = false, length = 255)
    private String pgId;

    @Column(name = "failure_reason")
    private String failureReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_reservation_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation reservation;

    private PaymentLog(PaymentStatus paymentStatus, PaymentType paymentType, Long amount, String pgId, String failureReason, Reservation reservation) {
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.amount = amount;
        this.pgId = pgId;
        this.failureReason = failureReason;
        this.reservation = reservation;
    }

    public static PaymentLog of(PaymentStatus paymentStatus, PaymentType paymentType, Long amount, String pgId, String failureReason, Reservation reservation) {
        return new PaymentLog(paymentStatus, paymentType, amount, pgId, failureReason, reservation);
    }
}
