package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdTenant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MdTenantRepositoryTest extends BaseTest {

    @Autowired
    private MdTenantRepository tenantRepository;


    @Test
    void testFindByTenantIdentifier() {

        MdTenant bhrthyund = tenantRepository.findByTenantIdentifier("bhrthyund");

        assertEquals(bhrthyund.getTenantIdentifier(), "bhrthyund");
    }

    @Test
    void testFindDistinctCities() {
        List<String> cities = tenantRepository.findDistinctCities("BHRT_HYUNDAI");

        cities.forEach(System.out::println);
    }

    @Test
    void testFindByMdOrganization_OrgIdentifier() {

        List<MdTenant> tenants = tenantRepository.findByMdOrganization_OrgIdentifier("BHRT_HYUNDAI");

        tenants.forEach(tenant -> System.out.println(tenant.getTenantIdentifier()));
    }

    @Test
    void testFindByCityAndMdOrganization_OrgIdentifier() {

        List<MdTenant> tenants = tenantRepository.findByCityAndIsActiveTrueAndMdOrganization_OrgIdentifier("HYDERABAD",
                "BHRT_HYUNDAI");

        tenants.forEach(tenant -> System.out.println(tenant.getTenantIdentifier()));

    }
}