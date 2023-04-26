package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HRMSIntegrationServiceTest extends BaseTest {

    @Autowired
    private HRMSIntegrationService hrmsIntegrationService;

    @Test
    void test_fetchAllEmployeesByManager() {
        final var integers = hrmsIntegrationService.fetchAllEmployeesByManager(48);
    }
}