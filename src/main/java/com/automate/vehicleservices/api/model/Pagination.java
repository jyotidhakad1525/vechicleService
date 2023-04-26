package com.automate.vehicleservices.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

/**
 * Pagination request object.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pagination {
    public static final int DEFAULT_SIZE = 10;
    public static final int DEFAULT_PAGE = 0;

    @PositiveOrZero
    private int page;

    @PositiveOrZero
    private int size;

    public int getPage() {
        if (page == 0)
            page = DEFAULT_PAGE;
        return page;
    }

    public int getSize() {
        if (size == 0)
            size = DEFAULT_SIZE;
        return size;
    }
}
