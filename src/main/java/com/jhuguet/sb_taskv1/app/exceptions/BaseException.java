package com.jhuguet.sb_taskv1.app.exceptions;

import java.util.Date;

public class BaseException extends Exception {

    private String message;
    private Date timestamp;

    public BaseException(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
