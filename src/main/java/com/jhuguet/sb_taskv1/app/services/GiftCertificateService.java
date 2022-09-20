package com.jhuguet.sb_taskv1.app.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    List<GiftCertificate> getAll();

    GiftCertificate get(int id) throws IdNotFound, InvalidIdInputInformation;

    GiftCertificate save(GiftCertificate certificate);

    GiftCertificate update(GiftCertificate certificate) throws IdNotFound, InvalidIdInputInformation;

    GiftCertificate delete(int id) throws IdNotFound, InvalidIdInputInformation, CertificateAssociatedException;

    GiftCertificate addTag(int certId, JsonPatch patch) throws JsonPatchException, JsonProcessingException, BaseException;
}
