package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/**
 * Public API Tag controller
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    private final Logger logger = Logger.getLogger(TagController.class.getName());

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Will retrieve Tag/s from Database
     *
     * @param id ID that if given will return specific Tag that has said ID, otherwise a list of Tags
     * @return will return pertaining List-of or single Tag retrieved from DB
     * @throws IdNotFound Exception thrown when given ID is not found
     */
    @ResponseBody
    @GetMapping("/{id}")
    public Tag get(@PathVariable(required = false) String id) throws IdNotFound {
        return tagService.get(Integer.parseInt(id));
    }

    @ResponseBody
    @GetMapping("/")
    public List<Tag> getAll() throws IdNotFound {
        return tagService.getAll();
    }

    /**
     * Will save a Tag into Database
     *
     * @param tag Given Tag to save into DB
     */
    @PostMapping
    public Tag save(@RequestBody Tag tag) throws MissingEntity {
        logger.info("Saving new tag: " + tag.getName());
        return tagService.save(tag);
    }

    /**
     * Updating function
     *
     * @param tag Tag to update
     * @return Updated Tag
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidInputInformation Exception thrown when given ID is incorrectly entered
     */
    @PutMapping
    public Tag update(@RequestBody Tag tag) throws IdNotFound, InvalidInputInformation {
        logger.info("Updating tag: " + tag.getId());
        return tagService.update(tag);
    }

    /**
     * Will drop a Tag from Database
     *
     * @param id ID of Tag to search and drop
     * @return Deleted Tag
     * @throws IdNotFound                     Exception thrown when given ID is not found
     * @throws CertificateAssociatedException Exception thrown when Tag is associated to a GiftCertificate and cannot be
     *                                        deleted
     */
    @DeleteMapping("/{id}")
    public Tag delete(@PathVariable String id) throws IdNotFound, CertificateAssociatedException {
        logger.info("Dropping tag: " + id);
        return tagService.delete(Integer.parseInt(id));
    }

}
