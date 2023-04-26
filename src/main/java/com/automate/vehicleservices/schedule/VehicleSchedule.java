package com.automate.vehicleservices.schedule;

import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;

/**
 * Interface that to be implemented byConcrete schedule classes.
 */
public interface VehicleSchedule {
    void schedule(ServiceVehicleDTO serviceVehicleDTO);
}
