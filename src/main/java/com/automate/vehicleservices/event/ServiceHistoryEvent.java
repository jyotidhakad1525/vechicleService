package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.repository.dtoprojection.VehicleServiceHistoryDTO;
import lombok.*;

/**
 * @author Chandrashekar V
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class ServiceHistoryEvent extends Event {
    private ServiceVehicleDTO vehicleDTO;
    private VehicleServiceHistoryDTO vehicleServiceHistoryDTO;

    public ServiceHistoryEvent(ServiceVehicleDTO vehicleDTO, VehicleServiceHistoryDTO vehicleServiceHistoryDTO) {
        super();
        this.vehicleDTO = vehicleDTO;
        this.vehicleServiceHistoryDTO = vehicleServiceHistoryDTO;
    }
}
