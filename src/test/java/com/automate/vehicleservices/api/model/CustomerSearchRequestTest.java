package com.automate.vehicleservices.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerSearchRequestTest {

    @Test
    void isSingleFieldSearch() {
        CustomerSearchRequest search = CustomerSearchRequest.builder().contactNumber("9901291028091").build();
        assertTrue(search.isSingleFieldSearch());
    }

    @Test
    void isMultiFieldSearch_FalseWhenOneOrNoneFieldsProvided() {
        CustomerSearchRequest search = CustomerSearchRequest.builder().contactNumber("9901291028091").build();
        assertFalse(search.isMultiFieldSearch());
    }

    @Test
    void isMultiFieldSearch_TrueWhenMoreThanOneFieldsProvided() {
        CustomerSearchRequest search =
                CustomerSearchRequest.builder().contactNumber("9901291028091").vehicleRegNumber("KA05MP1231").build();
        assertTrue(search.isMultiFieldSearch());
    }

    @Test
    void atLeastOneSearchFieldExists_FalseWhenNoFieldsProvided() {
        CustomerSearchRequest search = CustomerSearchRequest.builder().build();
        assertFalse(search.atLeastOneSearchFieldExists());
    }

    @Test
    void atLeastOneSearchFieldExists_TrueWhenOneOrMoreFieldsProvided() {
        CustomerSearchRequest search = CustomerSearchRequest.builder().contactNumber("9901291028091")
                .build();
        assertTrue(search.atLeastOneSearchFieldExists());
    }

}