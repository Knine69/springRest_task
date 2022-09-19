package com.jhuguet.sb_taskv1.app.exceptions;

public class InvalidIdInputInformation extends BaseException {

    public InvalidIdInputInformation() {
        super("The ID submitted does not comply with database query schema: Negative or null id");
    }

}
