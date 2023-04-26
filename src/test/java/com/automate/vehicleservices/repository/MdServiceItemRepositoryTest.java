package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceItem;
import com.automate.vehicleservices.entity.builder.MdServiceItemBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MdServiceItemRepositoryTest extends BaseTest {

    @Autowired
    private MdServiceItemRepository mdServiceItemRepository;


    @Test
    @Transactional
    void testSave() {
        MdServiceItem serviceItem = mdServiceItemRepository
                .save(MdServiceItemBuilder.aMdServiceItem().withName("test_item").withShortCode("TI")
                        .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund")).build());
        assertEquals(serviceItem.getName(), "test_item");
    }

}