package com.jigit.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String title;
    private int status;
    private String detail;
    private String instance;

    public static ErrorResponse of(ExceptionCode exceptionCode, String instance) {
        return new ErrorResponse(
                exceptionCode.getTitle(),
                exceptionCode.getHttpStatus().value(),
                exceptionCode.getDetail(),
                instance
        );
    }
}
