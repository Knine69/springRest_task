package com.jhuguet.sb_taskv1.services;

import com.jhuguet.sb_taskv1.models.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAllTags();

    Optional<Tag> getTag(int tagId);

    void saveTag(Tag tagToSave);

    void deleteTag(int tagId);
}
