package com.huydo.be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_KEY(8888, "invalid key", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "Username must be at 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "you do not have permission", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILED(1007, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

}