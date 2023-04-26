package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.repository.dtoprojection.SlotAvailability;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author Chandrashekar V
 */
class ServiceSlotAvailabilityRepositoryTest extends BaseTest {

    @Autowired
    private ServiceSlotAvailabilityRepository serviceSlotAvailabilityRepository;

    @Test
    void findByTenantIdAndServiceTypeAndServiceDate() {
        Iterable<SlotAvailability> slotAvailabilityList = serviceSlotAvailabilityRepository
                .fetchSlotsAvailability(1, LocalDate.now(), DayOfWeek.MONDAY.name());
    }
}