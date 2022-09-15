package com.jhuguet.sb_taskv1.services;

import com.jhuguet.sb_taskv1.models.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAllTags();

    Tag getTag(int id);

    Tag saveTag(Tag tag);

    Tag updateTag(Tag tag);

    Tag deleteTag(int id);
}
