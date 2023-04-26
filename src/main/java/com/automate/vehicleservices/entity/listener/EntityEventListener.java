package com.automate.vehicleservices.entity.listener;

import com.automate.vehicleservices.framework.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityEventListener {

    protected static EventPublisher eventPublisher;

    @Autowired
    public void setVehicleEventPublisher(EventPublisher eventPublisher) {
        EntityEventListener.eventPublisher = eventPublisher;
    }


}
