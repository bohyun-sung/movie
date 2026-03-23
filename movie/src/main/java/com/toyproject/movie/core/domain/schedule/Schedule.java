package com.toyproject.movie.core.domain.schedule;

import com.toyproject.movie.core.domain.movie.Movie;
import com.toyproject.movie.core.domain.theater.Theater;
import com.toyproject.movie.global.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule")
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_idx")
    private Long scheduleIdx;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "ticket_price", nullable = false)
    private Integer ticketPrice;

    @Column(name = "discount_type", nullable = false,
            comment = "0: 정상, 1: 조조할인")
    @Convert(converter = DiscountType.Converter.class)
    private DiscountType discountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_idx",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Theater theater;


    private Schedule(LocalDateTime startAt, LocalDateTime endAt, Integer ticketPrice, DiscountType discountType, Movie movie, Theater theater) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.ticketPrice = ticketPrice;
        this.discountType = discountType;
        this.movie = movie;
        this.theater = theater;
    }

    public static Schedule of(LocalDateTime startAt, LocalDateTime endAt, Integer ticketPrice, DiscountType discountType, Movie movie, Theater theater) {
        return new Schedule(startAt, endAt, ticketPrice, discountType, movie, theater);
    }

    /**
     * 티켓 가격 - 할인가격
     * @return 상영관 할인 적용 가격
     */
    public Integer discountTicketPrice() {
        return this.getTicketPrice() - this.discountType.getDiscount();
    }
}
