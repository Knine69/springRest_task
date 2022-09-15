package com.jhuguet.sb_taskv1.controller;

import com.jhuguet.sb_taskv1.models.Tag;
import com.jhuguet.sb_taskv1.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/tag")
public class TagController {

    private Logger logger = Logger.getLogger(TagController.class.getName());


    @Autowired
    private TagService tagService;

    @GetMapping("/")
    @ResponseBody
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Tag getTagById(@PathVariable String id) {
        return tagService.getTag(Integer.valueOf(id));
    }

    @PostMapping("/newTag")
    public void saveTag(@RequestBody Tag tag) {
        tagService.saveTag(tag);
    }

    @PutMapping
    public void updateTag(@RequestBody Tag tag) {
        tagService.updateTag(tag);
    }

    @DeleteMapping
    public void deleteTag(@RequestBody Tag tag) {
        tagService.deleteTag(tag.getId());
    }

}
