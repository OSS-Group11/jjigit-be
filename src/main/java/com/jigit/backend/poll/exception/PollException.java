package com.jigit.backend.poll.exception;

import com.jigit.backend.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PollException implements ExceptionCode {

    POLL_NOT_FOUND(HttpStatus.NOT_FOUND, "Poll Not Found", "The requested poll does not exist."),
    UNAUTHORIZED_POLL_ACCESS(HttpStatus.FORBIDDEN, "Unauthorized Access", "You do not have permission to access this poll."),
    INVALID_POLL_TITLE(HttpStatus.BAD_REQUEST, "Invalid Poll Title", "Poll title cannot be empty."),
    INSUFFICIENT_OPTIONS(HttpStatus.BAD_REQUEST, "Insufficient Options", "A poll must have at least 2 options."),
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Option Not Found", "The requested option does not exist.");

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
