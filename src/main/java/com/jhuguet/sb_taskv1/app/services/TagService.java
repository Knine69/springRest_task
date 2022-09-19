package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    Tag getTag(int id) throws IdNotFound, InvalidIdInputInformation;

    Tag saveTag(Tag tag);

    Tag updateTag(Tag tag) throws IdNotFound, InvalidIdInputInformation;

    Tag deleteTag(int id) throws InvalidIdInputInformation, IdNotFound;
}
