package com.jhuguet.sb_taskv1.app.exceptions;

public class MissingUserFields extends BaseException {
    public MissingUserFields() {
        super("One or more of the account fields is missing");
    }
}
