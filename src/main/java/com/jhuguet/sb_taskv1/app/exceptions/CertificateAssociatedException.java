package com.jhuguet.sb_taskv1.app.exceptions;

public class CertificateAssociatedException extends BaseException {
    public CertificateAssociatedException() {
        super("There are existing tags associated to this certificate. Unable to delete certificate.");
    }
}
