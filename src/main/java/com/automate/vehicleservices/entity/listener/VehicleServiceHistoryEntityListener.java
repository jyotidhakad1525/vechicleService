package com.automate.vehicleservices.entity.listener;

import com.automate.vehicleservices.entity.VehicleServiceHistory;
import com.automate.vehicleservices.event.ServiceHistoryEvent;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.repository.dtoprojection.VehicleServiceHistoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;

@Component
@Slf4j
public class VehicleServiceHistoryEntityListener extends EntityEventListener {

    @PostPersist
    @Async
    public void postPersist(VehicleServiceHistory target) {
        log.info(String.format("Triggering vehicle service history post persist event.%d", target.getId()));
        final var serviceVehicle = target.getServiceVehicle();

        final var serviceVehicleDTO =
                ServiceVehicleDTO.builder().id(serviceVehicle.getId())
                        .purchaseDate(serviceVehicle.getPurchaseDate())
                        .regNumber(serviceVehicle.getRegNumber())
                        .tenantId(serviceVehicle.getMdTenant().getTenantIdentifier()).build();

        final var vehicleServiceHistoryDTO = VehicleServiceHistoryDTO.builder()
                .serviceDate(target.getServiceDate())
                .kmReading(target.getKmReading())
                .serviceTypeId(target.getMdServiceType().getId())
                .tenantIdentifier(target.getMdTenant().getTenantIdentifier())
                .vehicleRegNumber(serviceVehicle.getRegNumber())
                .build();

        final var serviceHistoryEvent =
                ServiceHistoryEvent.builder().vehicleDTO(serviceVehicleDTO)
                        .vehicleServiceHistoryDTO(vehicleServiceHistoryDTO).build();

        eventPublisher.publish(serviceHistoryEvent);

    }

}
