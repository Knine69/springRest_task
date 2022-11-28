package com.jhuguet.sb_taskv1.app.exceptions;

public class UsernameNotFound extends BaseException {
    public UsernameNotFound() {
        super("Given username not found!");
    }
}
