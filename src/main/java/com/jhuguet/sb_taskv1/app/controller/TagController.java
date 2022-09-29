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

    @ResponseBody
    @GetMapping({"/", "/{id}"})
    public List<Tag> getTags(@PathVariable(required = false) String id) throws IdNotFound, InvalidIdInputInformation {
        if (id != null) {
            List<Tag> tag = new ArrayList<>();
            tag.add(tagService.get(Integer.valueOf(id)));
            return tag;
        }
        return tagService.getAll();
    }

    @PostMapping
    public void saveTag(@RequestBody Tag tag) {
        tagService.save(tag);
        logger.info("Saved new tag: " + tag.getName());
    }

    @PutMapping
    public void updateTag(@RequestBody Tag tag) throws IdNotFound, InvalidIdInputInformation {
        tagService.update(tag);
        logger.info("Updated tag: " + tag.getId());
    }

    @DeleteMapping("/drop/{id}")
    public void deleteTag(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation, CertificateAssociatedException {
        tagService.delete(Integer.parseInt(id));
        logger.info("Dropped tag: " + id);
    }

}
