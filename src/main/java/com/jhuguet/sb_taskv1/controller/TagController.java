package com.jhuguet.sb_taskv1.controller;

import com.jhuguet.sb_taskv1.models.Tag;
import com.jhuguet.sb_taskv1.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/all")
    @ResponseBody
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getTagById(@PathVariable String id) {
        Optional<Tag> tagOptional = tagService.getTag(Integer.valueOf(id));
        if (!tagOptional.isPresent()) {
            return "Tag does not exist in db";
        }

        return "Tag retrieved: " + tagOptional.get().getName();
    }

    @PostMapping("/save")
    public void saveTag(@RequestBody Tag tag) {
        tagService.saveTag(tag);
        System.out.println("Tag: " + tag.getName() + " was successfully created");
    }

    @PostMapping("/update")
    public void updateTag(@RequestBody Tag tag) {
        tagService.saveTag(tag);
        System.out.println("Tag: " + tag.getName() + " was successfully updated");
    }

    @PostMapping("/delete")
    public void deleteTag(@RequestBody Tag tag) {
        tagService.deleteTag(tag.getId());
        System.out.println("Tag: " + tag.getName() + " was successfully deleted");
    }

}
