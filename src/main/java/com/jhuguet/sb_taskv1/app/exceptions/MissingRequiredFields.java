package com.jhuguet.sb_taskv1.app.exceptions;

public class MissingRequiredFields extends BaseException {
    public MissingRequiredFields() {
        super("You're missing one or more fields required for inscription. Please revise them.");
    }
}
