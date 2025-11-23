package com.jigit.backend.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            HttpServletRequest request
    ) {
        ExceptionCode exceptionCode = ex.getExceptionCode();
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode, request.getRequestURI());

        return ResponseEntity
                .status(exceptionCode.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(
            InfrastructureException ex,
            HttpServletRequest request
    ) {
        ExceptionCode exceptionCode = ex.getExceptionCode();
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode, request.getRequestURI());

        return ResponseEntity
                .status(exceptionCode.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(
                CommonException.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(CommonException.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(errorResponse);
    }
}
