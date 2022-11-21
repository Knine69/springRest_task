package com.jhuguet.sb_taskv1.app.exceptions;

public class UnqualifiedAuthority extends BaseException {
    public UnqualifiedAuthority() {
        super("You do not possess the authority to make this request.");
    }
}
