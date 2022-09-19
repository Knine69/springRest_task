package com.jhuguet.sb_taskv1.app.exceptions;

public class ExceptionDetails {

    private long timestamp;
    private String description;

    public ExceptionDetails(long timestamp, String description) {
        this.timestamp = timestamp;
        this.description = description;
    }
}
