package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.TagsAssociatedException;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;

import java.util.List;

public interface GiftCertificateService {
    List<GiftCertificate> getAllCertificates();

    GiftCertificate getCertificate(int id) throws IdNotFound, InvalidIdInputInformation;

    GiftCertificate saveCertificate(GiftCertificate certificate);

    GiftCertificate updateCertificate(GiftCertificate certificate) throws IdNotFound, InvalidIdInputInformation;

    GiftCertificate deleteCertificate(int id) throws IdNotFound, InvalidIdInputInformation, TagsAssociatedException;
}
