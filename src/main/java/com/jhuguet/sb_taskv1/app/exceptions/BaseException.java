package com.jhuguet.sb_taskv1.app.exceptions;

import java.util.Date;

public class BaseException extends Exception {

    private String message;
    private String timestamp;

    public BaseException(String message) {
        this.message = message;
        this.timestamp = new Date().toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
