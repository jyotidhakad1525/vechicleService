package com.automate.vehicleservices.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeRepositoryTest extends BaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void test_findByActiveTrueAndTenant_Id() {

        final var employees = employeeRepository.findByActiveTrueAndTenant_Id(1);
    }

    @Test
    void findByActiveTrueAndMasterIdentifier() {
        final var employee = employeeRepository.findByActiveTrueAndMasterIdentifier(38);
        assertEquals(employee.getMasterIdentifier(), 38);
    }
}