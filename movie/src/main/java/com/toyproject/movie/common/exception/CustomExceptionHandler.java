package com.toyproject.movie.common.exception;

import com.toyproject.movie.common.response.ErrorResponse;
import com.toyproject.movie.global.enums.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j

@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     *  [커스텀 에러 처리]
     */
    @ExceptionHandler({ClientException.class, ServerException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(CommonException e) {

        HttpStatus status = e.getType().getHttpStatus();

        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), e.getMessage()));

    }

    /**
     *  [Bean Validation 예외 처리]
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String field = e.getBindingResult().getFieldError() != null ?
                e.getBindingResult().getFieldError().getField() : "unknown";
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.warn("Validation failed for field [{}]: {}", field, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message));
    }


    /**
     * [최상위 예외 처리]
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error("Unhandled Exception: ", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "서버 내부 오류가 발생했습니다 관리자에게 문의하세요"));
    }
}