package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.WrongSortOrder;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Public API Tag controller
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final Logger logger = Logger.getLogger(TagController.class.getName());
    private final PageResponse pageResponse = new PageResponse();

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Will retrieve Tag from Database
     *
     * @param id ID to look for in DB
     * @return will return pertaining Tag retrieved from DB
     * @throws IdNotFound Exception thrown when given ID is not found
     */
    @GetMapping("/{id}")
    public Tag get(@PathVariable(required = false) int id) throws IdNotFound {
        return tagService.get(id);
    }

    /**
     * Will give all existing Tags in database
     *
     * @param page Page requested to see
     * @param size Given size of a page
     * @param sort Sorting value of ascendant or descendant order
     * @return EntityModel of Page containing Tags
     * @throws IdNotFound              Exception thrown when given ID is not found
     * @throws InvalidInputInformation Exception thrown when given ID is incorrectly entered
     * @throws PageNotFound            Exception thrown when page requested doesn't exist
     */
    @GetMapping
    public EntityModel<Page<Tag>> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "3") int size,
                                         @RequestParam(defaultValue = "asc") String sort) throws
            InvalidInputInformation, PageNotFound, WrongSortOrder {
        pageResponse.validateInput(page, size);
        Page<Tag> tags = tagService.getAll(pageResponse.giveDynamicPageable(page, size, sort));
        return EntityModel.of(tags, linkTo(methodOn(TagController.class).getAll(page, size, sort)).withSelfRel());
    }

    /**
     * Will save a Tag into Database
     *
     * @param tag Given Tag to save into DB
     */
    @PostMapping
    public Tag save(@RequestBody Tag tag) throws MissingEntity, InvalidInputInformation {
        logger.info("Saving new tag: " + tag.getName());
        return tagService.save(tag);
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
    public Tag delete(@PathVariable int id) throws IdNotFound, CertificateAssociatedException {
        logger.info("Dropping tag: " + id);
        return tagService.delete(id);
    }

}
