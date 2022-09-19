package com.jhuguet.sb_taskv1.app.exceptions;

public class IdNotFound extends BaseException{
    public IdNotFound() {
        super("The ID submitted was not found in database");
    }
}
