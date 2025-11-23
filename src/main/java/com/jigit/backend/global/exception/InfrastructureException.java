package com.jigit.backend.global.exception;

import lombok.Getter;

@Getter
public class InfrastructureException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public InfrastructureException(ExceptionCode exceptionCode) {
        super(exceptionCode.getDetail());
        this.exceptionCode = exceptionCode;
    }

    public InfrastructureException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode.getDetail(), cause);
        this.exceptionCode = exceptionCode;
    }
}
