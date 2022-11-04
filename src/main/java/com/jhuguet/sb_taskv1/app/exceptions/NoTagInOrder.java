package com.jhuguet.sb_taskv1.app.exceptions;

public class NoTagInOrder extends BaseException {
    public NoTagInOrder() {
        super("There are no associated tags in the orders placed.");
    }
}
