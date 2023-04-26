package com.automate.vehicleservices.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginatedSearchResponse<T> {
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private int numberOfElements;
    private List<Sort> getSort;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean hasContent;
    private List<T> content;

    public PaginatedSearchResponse(Page page, List<T> list) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.getSort = page.getSort().get()
                .map(order -> Sort.builder().field(order.getProperty()).direction(order.getDirection().name())
                        .build()).collect(Collectors.toList());

        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.hasContent = page.hasContent();
        this.content = list;
    }

    public PaginatedSearchResponse(List resultList, int page, int size, int count) {
        this.content = resultList;
        this.number = page;
        if (size > 0) {
            final var i = count / size;
            this.totalPages = i == 0 ? 1 : i;
        }
        this.totalElements = count;
        this.size = size;
        if (CollectionUtils.isNotEmpty(resultList)) {
            this.numberOfElements = resultList.size();
            this.hasContent = true;
        }

        if (count / size > page + 1)
            this.hasNext = true;

        if (page != 0)
            this.hasPrevious = true;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sort {
        private String field;
        private String direction;
    }

}
