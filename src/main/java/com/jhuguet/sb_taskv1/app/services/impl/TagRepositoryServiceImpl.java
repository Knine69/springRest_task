package com.jhuguet.sb_taskv1.app.services.impl;

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
public class TagRepositoryServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagRepositoryServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() throws RuntimeException {
        return tagRepository.findAll();
    }

    public Tag getTag(int id) throws IdNotFound, InvalidIdInputInformation {
        return validateTag(id) ? tagRepository.findById(id).get() : null;
    }

    @Transactional
    public Tag saveTag(Tag tag) throws TransactionalException {
        tagRepository.save(tag);
        return tag;
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) throws IdNotFound, InvalidIdInputInformation {
        if (validateTag(tag.getId())) {
            tagRepository.save(tag);
        }
        return tag;
    }

    @Transactional
    public Tag deleteTag(int id) throws InvalidIdInputInformation, IdNotFound {
        Tag tag = getTag(id);
        if (tag != null) {
            tagRepository.deleteById(id);
        }
        return tag;
    }

    private boolean validateTag(int id) throws IdNotFound, InvalidIdInputInformation {
        if (id < 0) {
            throw new InvalidIdInputInformation();
        }

        if (!tagRepository.existsById(id)) {
            throw new IdNotFound();
        }

        return true;
    }

}
