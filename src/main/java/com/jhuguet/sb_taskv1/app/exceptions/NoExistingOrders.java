package com.jhuguet.sb_taskv1.app.exceptions;

public class NoExistingOrders extends BaseException {
    public NoExistingOrders() {
        super("There are currently no existing orders.");
    }
}
