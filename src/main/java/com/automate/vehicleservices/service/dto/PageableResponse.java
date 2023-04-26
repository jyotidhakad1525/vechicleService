package com.automate.vehicleservices.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponse<T> {

    List<T> items;

    private boolean nextPageExists;

    private long currentPage;

    private long totalPages;

    private long totalElements;

}
