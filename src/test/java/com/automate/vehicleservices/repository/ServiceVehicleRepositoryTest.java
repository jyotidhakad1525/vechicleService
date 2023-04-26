package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Chandrashekar V
 */
@Slf4j
class ServiceVehicleRepositoryTest extends BaseTest {

    @Autowired
    private ServiceVehicleRepository serviceVehicleRepository;

    @Test
    void findByRegNumber() {
        final var ka05MP0909 = serviceVehicleRepository.findByRegNumber("KA05MP0909");

        assertNotNull(ka05MP0909);

    }

    @Test
    void testFetchUnscheduledVehicles() {

        final var serviceVehicleDTOS =
                serviceVehicleRepository.fetchUnscheduledVehicles(Arrays.asList(ScheduleStatus.NEW.name(),
                        ScheduleStatus.IN_PROGRESS.name()));

        assertNotNull(serviceVehicleDTOS);
    }

    @Test
    void testFetchUnscheduledVehiclesPaginated() {

        Pageable pageable = PageRequest.of(0, 3);
        boolean vehiclesExists = true;
        while (vehiclesExists) {
            final var paginatedServiceVehicles =
                    serviceVehicleRepository.fetchUnscheduledVehiclesPaginated(Arrays.asList(ScheduleStatus.NEW.name(),
                            ScheduleStatus.IN_PROGRESS.name()), pageable);

            final var totalPages = paginatedServiceVehicles.getTotalPages();
            log.info(String.format("Total Pages: %d, current page: %d, Total Elements: %d, Number of Elements: %d",
                    totalPages,
                    paginatedServiceVehicles.getNumber(),
                    paginatedServiceVehicles.getTotalElements(),
                    paginatedServiceVehicles.getNumberOfElements()));
            if (!paginatedServiceVehicles.hasNext())
                vehiclesExists = false;

            pageable = paginatedServiceVehicles.nextPageable();

        }

    }

    @Test
    void testMarkVehiclesAsLost() {
        int updatedCount = serviceVehicleRepository.scanAndTagVehiclesAsInActiveOrLostBasedOnActivity(18);
        log.info(String.format("updated rows count: %d", updatedCount));
    }
}