package com.jhuguet.sb_taskv1.app.exceptions;

public class InvalidInputInformation extends BaseException {

    public InvalidInputInformation() {
        super("The information submitted does not comply with database query schema: Negative or null given.");
    }

}
