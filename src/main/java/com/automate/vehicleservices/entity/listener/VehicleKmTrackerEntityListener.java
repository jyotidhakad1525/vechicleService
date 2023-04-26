package com.automate.vehicleservices.entity.listener;

import com.automate.vehicleservices.entity.VehicleKmTracker;
import com.automate.vehicleservices.event.UpdateVehicleEvent;
import com.automate.vehicleservices.event.VehicleEventData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;

@Component
public class VehicleKmTrackerEntityListener extends EntityEventListener {

    @PostPersist
    @Async
    public void postPersist(VehicleKmTracker target) {
        eventPublisher.publish(UpdateVehicleEvent.builder()
                .eventData(new VehicleEventData(target.getServiceVehicle())).build());
    }

}
