package com.jhuguet.sb_taskv1.app.exceptions;

public class IdAlreadyInUse extends BaseException {
    public IdAlreadyInUse() {
        super("ID submitted is already in use by other entry.");
    }
}
