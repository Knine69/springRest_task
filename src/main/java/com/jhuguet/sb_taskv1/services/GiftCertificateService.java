package com.jhuguet.sb_taskv1.services;

import com.jhuguet.sb_taskv1.models.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    List<GiftCertificate> getAllCertificates();

    GiftCertificate getCertificate(int id);

    GiftCertificate saveCertificate(GiftCertificate certificate);

    GiftCertificate updateCertificate(GiftCertificate certificate);

    GiftCertificate deleteCertificate(int id);
}
