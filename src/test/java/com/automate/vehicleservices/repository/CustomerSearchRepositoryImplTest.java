package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.api.model.CustomerSearchRequest;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.SearchResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class CustomerSearchRepositoryImplTest extends BaseTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void test_search() {

        CustomerSearchRequest customerSearchRequest =
                CustomerSearchRequest.builder().vin("MH778KI7867").customerId("587").build();

        PaginatedSearchResponse<SearchResponseDTO> dtos = customerRepository.search(customerSearchRequest, 0, 10);
        dtos.getContent().stream().forEach(searchResponseDTO -> System.out.println(searchResponseDTO.toString()));
    }
}