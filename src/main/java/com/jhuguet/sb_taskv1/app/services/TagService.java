package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;

import java.util.List;

public interface TagService {
    List<Tag> getAll();

    Tag get(int id) throws IdNotFound, InvalidIdInputInformation;

    Tag save(Tag tag);

    Tag update(Tag tag) throws IdNotFound, InvalidIdInputInformation;

    Tag delete(int id) throws InvalidIdInputInformation, IdNotFound;

//    Tag addCertificate(int certId) throws BaseException;
}
