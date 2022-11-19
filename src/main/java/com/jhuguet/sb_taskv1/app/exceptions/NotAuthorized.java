package com.jhuguet.sb_taskv1.app.exceptions;

public class NotAuthorized extends BaseException {
    public NotAuthorized() {
        super("You are not authorized at the moment, please authenticate yourself first");
    }
}
