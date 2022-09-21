package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.services.TagService;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.TransactionalException;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;
    private GiftCertificateServiceImpl giftCertificateService;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, GiftCertificateServiceImpl giftCertificateService) {
        this.tagRepository = tagRepository;
        this.giftCertificateService = giftCertificateService;
    }

    public List<Tag> getAll() throws RuntimeException {
        return tagRepository.findAll();
    }

    public Tag get(int id) throws IdNotFound, InvalidIdInputInformation {
        validateTag(id);
        return tagRepository.findById(id).get();
    }

    @Transactional
    public Tag save(Tag tag) throws TransactionalException {
        tagRepository.save(tag);
        return tag;
    }

    @Override
    @Transactional
    public Tag update(Tag tag) throws IdNotFound, InvalidIdInputInformation {
        validateTag(tag.getId());
        tagRepository.save(tag);
        return tag;
    }

    //Do validation in case there are certificates associated
    @Transactional
    public Tag delete(int id) throws InvalidIdInputInformation, IdNotFound, CertificateAssociatedException {
        Tag tag = get(id);
        if(!tag.getCertificates().isEmpty()) {
            throw new CertificateAssociatedException();
        }
        tagRepository.deleteById(id);
        return tag;
    }

    private void validateTag(int id) throws IdNotFound, InvalidIdInputInformation {
        if (id < 0) {
            throw new InvalidIdInputInformation();
        }

        if (!tagRepository.existsById(id)) {
            throw new IdNotFound();
        }
    }
}
