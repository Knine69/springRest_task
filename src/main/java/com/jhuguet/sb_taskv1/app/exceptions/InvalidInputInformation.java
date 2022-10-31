package com.jhuguet.sb_taskv1.app.exceptions;

public class InvalidInputInformation extends BaseException {

    public InvalidInputInformation() {
        super("Incorrect input: Please input a positive value.");
    }

}
