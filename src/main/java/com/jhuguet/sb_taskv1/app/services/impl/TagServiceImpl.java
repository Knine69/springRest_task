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


    /**
     * Returns all GiftCertificates found in Database
     *
     * @return List of GiftCertificates
     */
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    /**
     * Returns a single GiftCertificate retrieved based on ID
     *
     * @param id Specific Tag ID to search for in Database
     * @return Will return pertaining Tag retrieved from Database
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    public Tag get(int id) throws IdNotFound, InvalidIdInputInformation {
        validateTag(id);
        return tagRepository.findById(id).get();
    }

    /**
     * Will save a Tag into Database
     *
     * @param tag Tag Object which will be saved
     * @return Tag object saved into Database
     */
    @Transactional
    public Tag save(Tag tag) {
        tagRepository.save(tag);
        return tag;
    }

    /**
     * Will update whole Tag entity in Database
     *
     * @param tag Tag to update
     * @return Updated Tag
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @Override
    @Transactional
    public Tag update(Tag tag) throws IdNotFound, InvalidIdInputInformation {
        validateTag(tag.getId());
        tagRepository.save(tag);
        return tag;
    }

    /**
     * Will delete specific Tag entity in Database
     *
     * @param id Specific Tag ID to look for and drop in Database
     * @return Tag dropped
     * @throws IdNotFound                     Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation      Exception thrown when given ID is incorrectly entered
     * @throws CertificateAssociatedException Exception thrown when Tag is associated to a GiftCertificate and cannot be
     *                                        deleted
     */
    @Transactional
    public Tag delete(int id) throws InvalidIdInputInformation, IdNotFound, CertificateAssociatedException {
        Tag tag = get(id);
        if (!tag.getCertificates().isEmpty()) {
            throw new CertificateAssociatedException();
        }
        tagRepository.deleteById(id);
        return tag;
    }

    /**
     * Will validate if ID is correctly entered and exists in Database
     *
     * @param id Specific ID looked for in Database
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    private void validateTag(int id) throws IdNotFound, InvalidIdInputInformation {
        if (id < 0) {
            throw new InvalidIdInputInformation();
        }

        if (!tagRepository.existsById(id)) {
            throw new IdNotFound();
        }
    }
}
