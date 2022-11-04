package com.jhuguet.sb_taskv1.app.exceptions;

public class OrderNotRelated extends BaseException {
    public OrderNotRelated() {
        super("Order with ID submitted is not related to User");
    }
}
