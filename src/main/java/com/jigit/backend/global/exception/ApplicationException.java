package com.jigit.backend.global.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public ApplicationException(ExceptionCode exceptionCode) {
        super(exceptionCode.getDetail());
        this.exceptionCode = exceptionCode;
    }

    public ApplicationException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode.getDetail(), cause);
        this.exceptionCode = exceptionCode;
    }
}
