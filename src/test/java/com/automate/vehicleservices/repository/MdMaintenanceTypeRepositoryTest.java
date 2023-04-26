package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.builder.MdMaintenanceTypeBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

class MdMaintenanceTypeRepositoryTest extends BaseTest {

    @Autowired
    private MdMaintenanceTypeRepository mdMaintenanceTypeRepository;

    @Test
    @Transactional
    @Rollback
    void testSave() {
        mdMaintenanceTypeRepository.save(MdMaintenanceTypeBuilder.aMdMaintenanceType()
                .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund"))
                .withType("Inspected and if Necessary Adjust, Top-up,Cleaning up - I").withShortCode("I")
                .withDescription("Inspected and if Necessary Adjust, Top-up,Cleaning up").build());


        mdMaintenanceTypeRepository.save(MdMaintenanceTypeBuilder.aMdMaintenanceType()
                .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund"))
                .withType("Replace - R").withShortCode("R")
                .withDescription("Replace").build());

        mdMaintenanceTypeRepository.save(MdMaintenanceTypeBuilder.aMdMaintenanceType()
                .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund"))
                .withType("Add - A").withShortCode("A")
                .withDescription("Add").build());


        mdMaintenanceTypeRepository.save(MdMaintenanceTypeBuilder.aMdMaintenanceType()
                .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund"))
                .withType("Cleaning & Replace if necessary - C").withShortCode("C")
                .withDescription("Cleaning & Replace if necessary").build());
    }

}