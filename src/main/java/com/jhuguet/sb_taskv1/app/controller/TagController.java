package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
     * @param id, if given will return specific Tag that has said ID
     * @return will return pertaining List-of or single Tag retrieved from DB
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @ResponseBody
    @GetMapping({"/", "/{id}"})
    public List<Tag> getTags(@PathVariable(required = false) String id) throws IdNotFound, InvalidIdInputInformation {
        if (id != null) {
            List<Tag> tag = new ArrayList<>();
            tag.add(tagService.get(Integer.parseInt(id)));
            return tag;
        }
        return tagService.getAll();
    }

    /**
     * @param tag Given Tag to save into DB
     */
    @PostMapping
    public Tag saveTag(@RequestBody Tag tag) {
        logger.info("Saving new tag: " + tag.getName());
        return tagService.save(tag);
    }

    /**
     * @param tag Tag to update
     * @return Updated Tag
     * @throws IdNotFound                Refer to line 39
     * @throws InvalidIdInputInformation Refer to line 40
     */
    @PutMapping
    public Tag updateTag(@RequestBody Tag tag) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Updating tag: " + tag.getId());
        return tagService.update(tag);
    }

    /**
     * @param id ID of Tag to search and drop
     * @return Deleted Tag
     * @throws IdNotFound                     Refer to line 39
     * @throws InvalidIdInputInformation      Refer to line 40
     * @throws CertificateAssociatedException Exception thrown when Tag is associated to a GiftCertificate and cannot be
     *                                        deleted
     */
    @DeleteMapping("/drop/{id}")
    public Tag deleteTag(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation, CertificateAssociatedException {
        logger.info("Dropping tag: " + id);
        return tagService.delete(Integer.parseInt(id));
    }

}
