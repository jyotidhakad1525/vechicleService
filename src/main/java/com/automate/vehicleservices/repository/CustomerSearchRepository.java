package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.api.model.CustomerSearchRequest;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.SearchResponseDTO;

public interface CustomerSearchRepository {
    PaginatedSearchResponse<SearchResponseDTO> search(CustomerSearchRequest customerSearchRequest, int page, int size);
}
