package com.jhuguet.sb_taskv1.app.exceptions;

public class WrongSortOrder extends BaseException {
    public WrongSortOrder() {
        super("Wrong order inserted, can't filter. Please insert a correct order: asc | desc");
    }
}
