package com.automate.vehicleservices.event;

import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEventData {
    private ServiceVehicleDTO serviceVehicle;

    public VehicleEventData(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = ServiceVehicleDTO.builder().id(serviceVehicle.getId())
                .regNumber(serviceVehicle.getRegNumber())
                .tenantId(serviceVehicle.getMdTenant().getTenantIdentifier())
                .currentKmReading(serviceVehicle.getCurrentKmReading())
                .purchaseDate(serviceVehicle.getPurchaseDate()).build();
    }
}
