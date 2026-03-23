package com.toyproject.movie.common.constants;

import com.toyproject.movie.common.exception.ServerException;
import com.toyproject.movie.global.enums.ExceptionType;

public final class ValidationMessage {

    private ValidationMessage() {
        throw new ServerException(ExceptionType.INTERNAL_SERVER_ERROR, "");
    }

    public static final String SEAT_NOT_NULL = "좌석을 선택해주세요.";
    public static final String SEAT_NOT_EMPTY = "좌석을 선택해주세요.";
    public static final String SCHEDULE_NOT_NULL = "상영관을 선택해주세요.";
    public static final String CLIENT_NOT_NULL = "고객 IDX를 입력해주세요.";
    public static final String AUDIENCE_NOT_NULL = "관람객의 연령을 선택해주세요.";
}
