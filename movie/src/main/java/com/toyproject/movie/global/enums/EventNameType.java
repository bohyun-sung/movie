package com.toyproject.movie.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventNameType {
    CREATED("생성"),
    UPDATE("수정"),
    ;
    private final String text;
}
