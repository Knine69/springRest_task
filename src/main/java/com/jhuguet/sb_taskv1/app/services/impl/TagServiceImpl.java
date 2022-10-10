package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag get(int id) throws IdNotFound, InvalidIdInputInformation {
        return tagRepository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Transactional
    public Tag save(Tag tag) {
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

    @Transactional
    public Tag delete(int id) throws InvalidIdInputInformation, IdNotFound, CertificateAssociatedException {
        Tag tag = get(id);
        if (!tag.getCertificates().isEmpty()) {
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
