package com.jhuguet.sb_taskv1.app.exceptions;

public class JwtExpired extends BaseException {
    public JwtExpired() {
        super("Session expired. Please authenticate yourself again");
    }
}
