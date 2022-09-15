package com.jhuguet.sb_taskv1.services.impl;

import com.jhuguet.sb_taskv1.models.GiftCertificate;
import com.jhuguet.sb_taskv1.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.TransactionalException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    public List<GiftCertificate> getAllCertificates() {
        return giftRepository.findAll();
    }

    //Create custom exceptions, replace throws statements
    public GiftCertificate getCertificate(int certificateId) {
        return giftRepository.findById(certificateId).get();
    }

    @Transactional
    public GiftCertificate saveCertificate(GiftCertificate giftCertificate) throws TransactionalException {
        String localDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        giftCertificate.setCreateDate(localDate);
        giftCertificate.setLastUpdateDate(localDate);
        giftRepository.save(giftCertificate);

        return giftCertificate;
    }

    @Override
    public GiftCertificate updateCertificate(GiftCertificate certificate) {
        if (validateCertificate(certificate.getId())) {
            giftRepository.save(certificate);
        }
        return certificate;
    }

    @Transactional
    public GiftCertificate deleteCertificate(int id) throws TransactionalException {
        GiftCertificate certificate = new GiftCertificate();
        if (validateCertificate(id)) {
            certificate = getCertificate(id);
            giftRepository.deleteById(id);
        }
        return certificate;
    }

    private boolean validateCertificate(int id) throws RuntimeException {
        return giftRepository.existsById(id);
    }
}
