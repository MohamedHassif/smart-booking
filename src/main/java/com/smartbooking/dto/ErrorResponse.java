package com.smartbooking.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public ErrorResponse(int status, String message,Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}
