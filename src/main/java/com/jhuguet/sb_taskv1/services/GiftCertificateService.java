package com.jhuguet.sb_taskv1.services;

import com.jhuguet.sb_taskv1.models.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    List<GiftCertificate> getAllCertificates();

    Optional<GiftCertificate> getCertificate(int certificateId);

    void saveCertificate(GiftCertificate giftCertificate);

    void deleteCertificate(int giftCertificateId);
}
