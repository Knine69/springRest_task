package com.jhuguet.sb_taskv1.services.impl;

import com.jhuguet.sb_taskv1.models.Tag;
import com.jhuguet.sb_taskv1.repositories.TagRepository;
import com.jhuguet.sb_taskv1.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.TransactionalException;
import java.util.List;
import java.util.Optional;

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

    public Tag getTag(int id) throws RuntimeException {
        return tagRepository.findById(id).get();
    }

    @Transactional
    public Tag saveTag(Tag tag) throws TransactionalException {
        tagRepository.save(tag);
        return tag;
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) throws TransactionalException {
        Optional<Tag> validateTag = tagRepository.findById(tag.getId());
        if (validateTag.isPresent()) {
            tagRepository.save(tag);
        }

        return tag;
    }

    @Transactional
    public Tag deleteTag(int id) throws TransactionalException {
        Tag tag = new Tag();
        if (validateTag(id) && tag.getCertificates() != null) {
            tag = getTag(id);
            tagRepository.deleteById(id);
        }
        return tag;
    }

    private boolean validateTag(int id) throws RuntimeException {
        return tagRepository.existsById(id);
    }

}
