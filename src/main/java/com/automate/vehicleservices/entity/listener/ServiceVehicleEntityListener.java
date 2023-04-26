package com.automate.vehicleservices.entity.listener;

import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.event.NewVehicleEvent;
import com.automate.vehicleservices.event.VehicleEventData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;

@Component
public class ServiceVehicleEntityListener extends EntityEventListener {

    @PostPersist
    @Async
    public void postPersist(ServiceVehicle target) {
        eventPublisher.publish(NewVehicleEvent.builder()
                .eventData(new VehicleEventData(target)).build());
    }

}
