package com.jigit.backend.user.exception;

import com.jigit.backend.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserException implements ExceptionCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found", "The requested user does not exist."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "Duplicate Username", "The username is already taken."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid Password", "The password format is invalid."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid Credentials", "Username or password is incorrect.");

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
