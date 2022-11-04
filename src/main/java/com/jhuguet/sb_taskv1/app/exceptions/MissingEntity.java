package com.jhuguet.sb_taskv1.app.exceptions;

public class MissingEntity extends BaseException {
    public MissingEntity() {
        super("Intended entity is missing, cannot proceed further.");
    }
}
