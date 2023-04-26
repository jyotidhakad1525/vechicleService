package com.automate.vehicleservices.service.facade;

import com.automate.vehicleservices.api.model.CustomerSearchRequest;
import com.automate.vehicleservices.repository.BaseTest;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.SearchResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class CustomerSearchFacadeTest extends BaseTest {

    @Autowired
    private CustomerSearchFacade customerSearchFacade;

    @Test
    void testSearch_WhenOnlyCustomerName_isProvided() {
        PaginatedSearchResponse<SearchResponseDTO> search = customerSearchFacade
                .search(CustomerSearchRequest.builder().customerName("o").build(), "bhrthyund", 0, 10);

        if (null != search)
            search.getContent().forEach(searchResponseDTO -> log.info(searchResponseDTO.toString() + "\n"));
    }

    @Test
    void testSearch_WhenOnlyCustomerId_isProvided() {
        PaginatedSearchResponse<SearchResponseDTO> search = customerSearchFacade
                .search(CustomerSearchRequest.builder().customerId("107").build(), "bhrthyund", 0, 10);

        if (null != search)
            search.getContent().forEach(searchResponseDTO -> log.info(searchResponseDTO.toString() + "\n"));
    }

    @Test
    void testSearch_WhenOnlyCustomerContactNumber_isProvided() {
        PaginatedSearchResponse<SearchResponseDTO> search = customerSearchFacade
                .search(CustomerSearchRequest.builder().contactNumber("9901055544").build(), "bhrthyund", 0, 10);

        if (null != search)
            search.getContent().forEach(searchResponseDTO -> log.info(searchResponseDTO.toString() + "\n"));
    }

    @Test
    void testSearch_WhenOnlyVehicleRegNumber_isProvided() {
        PaginatedSearchResponse<SearchResponseDTO> search = customerSearchFacade
                .search(CustomerSearchRequest.builder().vehicleRegNumber("AP07121212").build(), "bhrthyund", 0, 10);

        if (null != search)
            search.getContent().forEach(searchResponseDTO -> log.info(searchResponseDTO.toString() + "\n"));
    }

    @Test
    void testSearch_WhenMultipleFieldsProvided() {
        PaginatedSearchResponse<SearchResponseDTO> search = customerSearchFacade
                .search(CustomerSearchRequest.builder().vin("MH778KI7867").customerId("587").build(),
                        "bhrthyund", 0, 10);

        if (null != search)
            search.getContent().forEach(searchResponseDTO -> log.info(searchResponseDTO.toString() + "\n"));
    }
}