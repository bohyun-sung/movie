package com.toyproject.movie.common.constants;

import com.toyproject.movie.common.exception.ServerException;
import com.toyproject.movie.global.enums.ExceptionType;

public final class ValidationMessage {

    // ### NotNull && NotEmpty && NotBlank ###
    // 영화
    public static final String MOVIE_IDX_NOT_NULL = "영화 idx를 입력해주세요.";
    public static final String MOVIE_TITLE_NOT_BLANK = "영화 제목을 입력해주세요.";
    public static final String RELEASE_DATE_NOT_NULL = "개봉일을 입력해주세요.";
    // 좌석
    public static final String SEAT_NOT_NULL = "좌석을 선택해주세요.";
    public static final String SEAT_NOT_EMPTY = "좌석을 선택해주세요.";
    public static final String LAST_SEAT_ROW_NOT_BLANK = "마지막 행을 입력해주세요.";
    public static final String LAST_SEAT_COLUM_NOT_NULL = "마지막 열을 입력해주세요.";
    // 상영관
    public static final String THEATER_IDX_NOT_NULL = "상영관 idx를 입력해주세요.";
    public static final String SCHEDULE_NOT_NULL = "상영관을 선택해주세요.";
    public static final String RUNTIME_NOT_NULL = "상영 시간을 입력해주세요.";
    public static final String AUDIENCE_NOT_NULL = "관람객의 연령을 선택해주세요.";
    public static final String CINEMA_NAME_NOT_BLANK = "영화관 지점명을 입력해주세요.";
    public static final String THEATER_NAME_NOT_BLANK = "상영관명을 입력해주세요.";
    public static final String START_AT_NOT_NULL = "상영 시작 시간을 입력해주세요.";
    public static final String TICKET_PRICE_NOT_NULL = "관람료를 입력해주세요.";
    // 고객
    public static final String CLIENT_NOT_NULL = "고객 IDX를 입력해주세요.";

    // ### Max && Min ###
    // 좌석
    public static final String LAST_SEAT_COLUM_MAX_MIN = "열 번호는 1에서 16 사이의 숫자를 입력해주세요.";

    // ### Future ###
    // 상영관
    public static final String START_AT_FUTURE = "상영 시작 시간은 현재보다 미래시간을 입력해주세요.";

    // ### PositiveOrZero ###
    // 상영관
    public static final String TICKET_PRICE_POSITIVE_OR_ZERO = "관람료는 0원 이상 입력해주세요.";


    private ValidationMessage() {
        throw new ServerException(ExceptionType.INTERNAL_SERVER_ERROR, "");
    }
}
