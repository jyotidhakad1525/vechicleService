package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceTypeCriteria;
import com.automate.vehicleservices.entity.builder.MdServiceTypeCriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Chandrashekar V
 */
class MdServiceTypeCriteriaRepositoryTest extends BaseTest {

    @Autowired
    private MdServiceTypeCriteriaRepository mdServiceTypeCriteriaRepository;
    @Autowired
    private MdServiceTypeRepository mdServiceTypeRepository;

    @Test
    @Transactional
    @Rollback
    void testSave() {
        saveCriteria(2, 0, 4999, (short) 0, (short) 30, 0, (float) 0.5, "First Free Service Criteria");
        saveCriteria(3, 5000, 14999, (short) 31, (short) 365, (float) 0.5, (float) 1.5, "Second Free Service Criteria");
        saveCriteria(4, 15000, 24999, (short) 366, (short) 730, (float) 1.5, (float) 2.5, "Third Free Service " +
                "Criteria");
        saveCriteria(5, 25000, 34999, (short) 730, (short) 1095, (float) 2.5, (float) 3.5, "PMS 30");
        saveCriteria(6, 35000, 44999, (short) 1096, (short) 1460, (float) 3.5, (float) 4.5, "PMS 40");
        saveCriteria(7, 45000, 54999, (short) 1461, (short) 1825, (float) 4.5, (float) 5.5, "PMS 50");
        saveCriteria(8, 55000, 64999, (short) 1826, (short) 2190, (float) 5.5, (float) 6.5, "PMS 60");
        saveCriteria(9, 65000, 74999, (short) 2191, (short) 2555, (float) 6.5, (float) 7.5, "PMS 70");
        saveCriteria(10, 75000, 84999, (short) 2556, (short) 2920, (float) 7.5, (float) 8.5, "PMS 80");
        saveCriteria(11, 85000, 94999, (short) 2921, (short) 3285, (float) 8.5, (float) 9.5, "PMS 90");
        saveCriteria(12, 95000, 104999, (short) 3286, (short) 3650, (float) 9.5, (float) 10.5, "PMS 100");
    }


    private void saveCriteria(int serviceTypeId, int mileageFrom, int mileageTo, short durationInDaysFrom,
            short durationInDaysTo
            , float durationYearFrom, float durationYearTo, String description) {
        MdServiceTypeCriteria mdServiceTypeCriteria = mdServiceTypeCriteriaRepository
                .save(MdServiceTypeCriteriaBuilder.aMdServiceTypeCriteria()
                        .withMdServiceType(mdServiceTypeRepository.findById(serviceTypeId).get())
                        .withMileageFrom(mileageFrom).withMileageTo(mileageTo).withDurationDaysFrom(durationInDaysFrom)
                        .withDurationDaysTo(durationInDaysTo)
                        .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund"))
                        .withDescription(description)
                        .build());

        assertNotNull(mdServiceTypeCriteria);
    }

}