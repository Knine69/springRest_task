package com.jhuguet.sb_taskv1.app.exceptions;

public class TagsAssociatedException extends BaseException {
    public TagsAssociatedException() {
        super("There are existing tags associated to this certificate. Unable to delete certificate.");
    }
}
