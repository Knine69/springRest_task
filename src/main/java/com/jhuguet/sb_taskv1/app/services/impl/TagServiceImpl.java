package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.repositories.TagRepository;
import com.jhuguet.sb_taskv1.app.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Page<Tag> getAll(Pageable pageable) throws PageNotFound {
        Page<Tag> page = tagRepository.findAll(pageable);

        if (page.getTotalPages() <= pageable.getPageNumber()) {
            throw new PageNotFound();
        }

        return page;
    }

    public Tag get(int id) throws IdNotFound {
        return tagRepository.findById(id).orElseThrow(IdNotFound::new);
    }

    @Transactional
    public Tag save(Tag tag) throws MissingEntity {
        if (!Objects.isNull(tag)) {
            tagRepository.save(tag);
        } else {
            throw new MissingEntity();
        }
        return tag;
    }

    @Transactional
    public Tag delete(int id) throws IdNotFound, CertificateAssociatedException {
        Tag tag = get(id);
        if (!tag.getCertificates().isEmpty()) {
            throw new CertificateAssociatedException();
        }
        tagRepository.deleteById(id);
        return tag;
    }
}
