package com.jhuguet.sb_taskv1.app.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    List<GiftCertificate> getAll();

    GiftCertificate get(int id) throws IdNotFound, InvalidIdInputInformation;

    GiftCertificate save(GiftCertificate certificate);

    GiftCertificate update(int id, Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation;

    GiftCertificate delete(int id) throws IdNotFound, InvalidIdInputInformation, CertificateAssociatedException;

    GiftCertificate updateTags(int id, List<Tag> tag) throws IdNotFound, InvalidIdInputInformation;
}
