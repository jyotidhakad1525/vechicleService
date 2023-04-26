package com.automate.vehicleservices.event.listeners;

import com.automate.vehicleservices.event.NewVehicleEvent;
import com.automate.vehicleservices.event.ServiceHistoryEvent;
import com.automate.vehicleservices.event.UpdateVehicleEvent;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.schedule.v1.VehicleSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * VehicleScheduleEventListener : Listens to events that impact vehicle service schedule, and trigger respective service
 * schedule workflow.
 */
@Component
@Slf4j
public class VehicleEventListener {

    private final VehicleSchedule vehicleScheduleForNew;
    private final VehicleSchedule vehicleScheduleForUpdate;

    public VehicleEventListener(@Qualifier("NewVehicleSchedulingEngineV1") VehicleSchedule vehicleScheduleForNew,
                                @Qualifier("UpdatedVehicleSchedulingEngineV1") VehicleSchedule vehicleScheduleForUpdate) {
        this.vehicleScheduleForNew = vehicleScheduleForNew;
        this.vehicleScheduleForUpdate = vehicleScheduleForUpdate;
    }

    @EventListener(value = {NewVehicleEvent.class})
    public void triggerScheduleWhenVehicleServiceHistoryUpdated(NewVehicleEvent newVehicleEvent) {
        log.info(String.format("Handling new vehicle event: ID: %s", newVehicleEvent.getEventId()));
        scheduleService(newVehicleEvent.getEventData().getServiceVehicle(), vehicleScheduleForNew);
    }

    @EventListener(value = {UpdateVehicleEvent.class})
    public void triggerScheduleWhenVehicleServiceHistoryUpdated(UpdateVehicleEvent updateVehicleEvent) {
        log.info(String.format("Handling Update vehicle event: ID: %s", updateVehicleEvent.getEventId()));
        scheduleService(updateVehicleEvent.getEventData().getServiceVehicle(), vehicleScheduleForUpdate);
    }

    @EventListener(value = {ServiceHistoryEvent.class})
    public void triggerScheduleWhenVehicleServiceHistoryUpdated(ServiceHistoryEvent serviceHistoryEvent) {
        log.info(String.format("Handling service history event: ID: %s", serviceHistoryEvent.getEventId()));
        scheduleService(serviceHistoryEvent.getVehicleDTO(), vehicleScheduleForUpdate);
    }

    private void scheduleService(ServiceVehicleDTO serviceVehicleDTO, VehicleSchedule vehicleSchedule) {
        log.info(String.format("Vehicle schedule event for vehicle %s", serviceVehicleDTO.getRegNumber()));
        vehicleSchedule.schedule(serviceVehicleDTO);
    }


}
