package com.jhuguet.sb_taskv1.app.exceptions;

public class WrongCredentials extends BaseException {
    public WrongCredentials() {
        super("Either username or password is incorrect. Please try again.");
    }
}
