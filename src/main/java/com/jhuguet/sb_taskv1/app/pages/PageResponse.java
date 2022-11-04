package com.jhuguet.sb_taskv1.app.pages;

import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageResponse {

    public void validateInput(int page, int size) throws InvalidInputInformation {
        if (page < 1 || size < 1) {
            throw new InvalidInputInformation();
        }
    }

    public PageRequest giveDynamicPageable(int page, int size, String sorted) {
        return PageRequest.of(page - 1, size, sorted.equalsIgnoreCase("asc") ?
                Sort.by("id").ascending() :
                Sort.by("id").descending()
        );
    }
}
