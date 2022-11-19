package com.jhuguet.sb_taskv1.app.pages;

import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.WrongSortOrder;
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

    public PageRequest giveDynamicPageable(int page, int size, String sorted) throws WrongSortOrder {
        return PageRequest.of(page - 1, size, validateSorting(sorted) ? Sort.by("id").ascending() :
                Sort.by("id").descending());
    }

    private boolean validateSorting(String sorted) throws WrongSortOrder {
        if (!(sorted.equalsIgnoreCase("asc") || sorted.equalsIgnoreCase("desc"))) {
            throw new WrongSortOrder();
        }
        return sorted.equalsIgnoreCase("asc");
    }
}
