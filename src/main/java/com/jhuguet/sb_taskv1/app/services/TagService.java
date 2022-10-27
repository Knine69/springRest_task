package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {
    Page<Tag> getAll(Pageable pageable);

    Tag get(int id) throws IdNotFound;

    Tag save(Tag tag) throws MissingEntity;

    Tag delete(int id) throws IdNotFound, CertificateAssociatedException;

}
