package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.builder.MdServiceCategoryBuilder;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.repository.dtoprojection.MdServiceCategoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Slf4j
class MdServiceCategoryRepositoryTest extends BaseTest {

    @Autowired
    private MdServiceCategoryRepository mdServiceCategoryRepository;
    @Autowired
    private MdTenantRepository mdTenantRepository;

    @Test
    @Transactional
    void testSave() {
        MdServiceCategory serviceCategory = mdServiceCategoryRepository
                .save(MdServiceCategoryBuilder.aMdServiceCategory().withCategoryName("test-category").withServiceGroup(
                        ServiceGroup.REGULAR_MAINTENANCE)
                        .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund")).build());
        assertEquals(serviceCategory.getCategoryName(), "test-category");
    }

    @Test
    @Transactional
    void testFindByMdTenantAndIsActive() {
        List<MdServiceCategoryDTO> mdServiceCategoryDTOS = mdServiceCategoryRepository.findByMdTenantAndActive(
                mdTenantRepository.findByTenantIdentifier("bhrthyund"), true);
        mdServiceCategoryDTOS.forEach(dto -> log.info(dto.getCategoryName() + " " + dto.getId() + " " + dto.getServiceGroup()));
        assertNotNull(mdServiceCategoryDTOS);
    }

}