package com.automate.vehicleservices.schedule.v1;

import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("NewVehicleSchedulingEngineV1")
public class NewVehicleSchedule extends AbstractVehicleSchedule {

    @Override
    protected void preRequisite(ServiceVehicleDTO serviceVehicleDTO) {
        if (isActiveScheduleExists(serviceVehicleDTO)) {
            log.warn(String.format("There is an ongoing schedule exists for this vehicle %s:",
                    serviceVehicleDTO.getRegNumber()));
            return;
        }

    }
}
