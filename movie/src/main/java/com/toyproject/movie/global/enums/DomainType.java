package com.toyproject.movie.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainType {
    RESERVATION("예약"),
    ;

    private final String text;
}
