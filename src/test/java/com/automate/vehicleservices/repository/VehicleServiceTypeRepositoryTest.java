package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.repository.dtoprojection.IServiceTypeDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Chandrashekar V
 */
class VehicleServiceTypeRepositoryTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(VehicleServiceTypeRepositoryTest.class);
    @Autowired
    private MdServiceTypeRepository mdServiceTypeRepository;

    @Autowired
    private MdTenantRepository mdTenantRepository;

    @Test
    public void testFindByTenantAndIsActive() {

        List<IServiceTypeDTO> serviceTypes = mdServiceTypeRepository.findByMdTenantAndActive(mdTenantRepository.findByTenantIdentifier("bhrthyund"), true);

        serviceTypes.forEach(type ->
                log.info(type.getId() + " " + type.getServiceName() + " "
                + type.getMdServiceCategory_id() + " " + type.getMdServiceCategory_categoryName()
                + " " + type.getMdServiceCategory_serviceGroup()));
        assertNotNull(serviceTypes);
    }
}