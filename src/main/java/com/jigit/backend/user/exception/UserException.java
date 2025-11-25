package com.jigit.backend.user.exception;

import com.jigit.backend.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserException implements ExceptionCode {

    // User-related exceptions
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found", "The requested user does not exist."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "Duplicate Username", "The username is already taken."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid Password", "The password format is invalid."),

    // Authentication exceptions
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid Credentials", "Username or password is incorrect."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token", "The provided token is invalid."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired Token", "The provided token has expired."),
    TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Token Generation Failed", "Failed to generate authentication token.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String detail;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
