package com.jhuguet.sb_taskv1.app.exceptions;

public class CertificateAssociatedException extends BaseException {
    public CertificateAssociatedException() {
        super("There are existing certificates with this tag associated. Unable to delete tag.");
    }
}
