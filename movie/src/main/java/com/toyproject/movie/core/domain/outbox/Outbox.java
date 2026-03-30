package com.toyproject.movie.core.domain.outbox;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outbox")
@Entity
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type", comment = "ex) MOVIE, RESERVATION")
    private String aggregateType;

    @Column(name = "aggregate_id", comment = "해당 도메인의 PK")
    private Long aggregateId;

    @Column(name = "event_type", comment = "ex) RESERVATION_CREATED")
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "TEXT", comment = "실제 데이터 (JSON String)")
    private String payload;


    private Outbox(String aggregateType, Long aggregateId, String eventType, String payload) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
    }

    public static Outbox of(String aggregateType, Long aggregateId, String eventType, String payload) {
        return new Outbox(aggregateType, aggregateId, eventType, payload);
    }
}
