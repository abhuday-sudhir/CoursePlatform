package com.project.CoursePlatform.exception;

import java.time.Instant;

public class ApiError {

    private String error;
    private String message;
    private Instant timestamp;

    public ApiError(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public Instant getTimestamp() { return timestamp; }
}
