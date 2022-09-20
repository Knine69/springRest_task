package com.jhuguet.sb_taskv1.app.exceptions;

import lombok.Getter;

import java.util.Date;

@Getter
public class ExceptionDetails {

    private Date timestamp;
    private String description;

    public ExceptionDetails(String description) {
        this.timestamp = new Date();
        this.description = description;
    }

}
