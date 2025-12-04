package com.jigit.backend.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            HttpServletRequest request
    ) {
        ExceptionCode exceptionCode = ex.getExceptionCode();
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode, request.getRequestURI());

        log.warn("Application exception occurred - URI: {}, Method: {}, Error: {}",
                request.getRequestURI(),
                request.getMethod(),
                exceptionCode.getTitle());

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

        log.error("Infrastructure exception occurred - URI: {}, Method: {}, Error: {}",
                request.getRequestURI(),
                request.getMethod(),
                exceptionCode.getTitle(),
                ex);

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

        log.error("Unexpected exception occurred - URI: {}, Method: {}, Message: {}",
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);

        return ResponseEntity
                .status(CommonException.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(errorResponse);
    }
}
