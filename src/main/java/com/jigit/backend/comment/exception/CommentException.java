package com.jigit.backend.comment.exception;

import com.jigit.backend.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CommentException implements ExceptionCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment Not Found", "The requested comment does not exist."),
    INVALID_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, "Invalid Comment Content", "Comment content cannot be empty."),
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "Unauthorized Access", "You do not have permission to modify this comment.");

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
