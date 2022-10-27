package com.jhuguet.sb_taskv1.app.pages;

import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageResponse {

    public void validateInput(int page, int size) throws InvalidInputInformation {
        if (page < 0 || size < 0) {
            throw new InvalidInputInformation();
        }
    }

    public PageRequest giveDynamicPageable(int page, int size, boolean sorted) {
        return PageRequest.of(page, size, sorted ? Sort.by("id").ascending() :
                Sort.by("id").descending());
    }
}
