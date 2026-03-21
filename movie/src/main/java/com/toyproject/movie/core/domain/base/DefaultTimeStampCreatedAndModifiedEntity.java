package com.toyproject.movie.core.domain.base;

import jakarta.persistence.Column;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public abstract class DefaultTimeStampCreatedAndModifiedEntity extends DefaultTimeStampCreatedEntity {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(name = "updt", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP COMMENT '변경시간'")
    protected LocalDateTime updt;
}
