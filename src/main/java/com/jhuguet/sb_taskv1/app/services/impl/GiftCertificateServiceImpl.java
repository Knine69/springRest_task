package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.TagsAssociatedException;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
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
    private TagRepository tagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftRepository, TagRepository tagRepository) {
        this.giftRepository = giftRepository;
        this.tagRepository = tagRepository;
    }

    public List<GiftCertificate> getAllCertificates() {
        return giftRepository.findAll();
    }

    public GiftCertificate getCertificate(int id) throws IdNotFound, InvalidIdInputInformation {
        return validateCertificate(id) ? giftRepository.findById(id).get() : null;
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
    public GiftCertificate updateCertificate(GiftCertificate certificate) throws IdNotFound, InvalidIdInputInformation {
        if (validateCertificate(certificate.getId())) {
            certificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            giftRepository.save(certificate);
        }
        return certificate;
    }

    @Transactional
    public GiftCertificate deleteCertificate(int id) throws IdNotFound, InvalidIdInputInformation, TagsAssociatedException {
        GiftCertificate certificate = getCertificate(id);
        if(certificate.getAssociatedTag().size() > 0){
            throw new TagsAssociatedException();
        }else {
            giftRepository.deleteById(id);
        }

        return certificate;
    }

    @Override
    public Tag addTagToCertificate(int tagId, int certificateId) throws IdNotFound, InvalidIdInputInformation {
        GiftCertificate certificate;
        Tag tag = null;
        if(tagRepository.existsById(tagId)){
            tag = tagRepository.findById(tagId).get();
            certificate = getCertificate(certificateId);
            List<Tag> tags = certificate.getAssociatedTag();
            tags.add(tag);
            certificate.setAssociatedTag(tags);
        }

        return tag;
    }

    private boolean validateCertificate(int id) throws IdNotFound, InvalidIdInputInformation {
        if (id < 0) {
            throw new InvalidIdInputInformation();
        }

        if (!giftRepository.existsById(id)) {
            throw new IdNotFound();
        }

        return true;
    }
}
