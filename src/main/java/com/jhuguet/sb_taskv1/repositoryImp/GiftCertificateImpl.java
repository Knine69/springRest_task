package com.jhuguet.sb_taskv1.repositoryImp;

import com.jhuguet.sb_taskv1.models.GiftCertificate;
import com.jhuguet.sb_taskv1.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.services.GiftCertificateService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.TransactionalException;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateImpl implements GiftCertificateService {

    private GiftCertificateRepository giftRepository;

    public GiftCertificateImpl(GiftCertificateRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    public List<GiftCertificate> getAllCertificates() {
        return giftRepository.findAll();
    }

    public Optional<GiftCertificate> getCertificate(int certificateId) throws RuntimeException {
        return giftRepository.findById(certificateId);
    }

    //Due to merging performed by JPA, it is not necessary to have update method, as this is done in save method
    @Transactional
    public void saveCertificate(GiftCertificate giftCertificate) throws TransactionalException {
        giftRepository.save(giftCertificate);
    }

    @Transactional
    public void deleteCertificate(int giftCertificateId) throws TransactionalException {
        giftRepository.deleteById(giftCertificateId);
    }
}
