package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAll();

    Tag get(int id) throws IdNotFound, InvalidIdInputInformation;

    Tag save(Tag tag);

    Tag update(Tag tag) throws IdNotFound, InvalidIdInputInformation;

    Tag delete(int id) throws InvalidIdInputInformation, IdNotFound, CertificateAssociatedException;

//    Tag addCertificate(int certId) throws BaseException;
}
