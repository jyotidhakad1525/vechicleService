package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateVehicleEvent extends Event {
    private VehicleEventData eventData;

    @Builder
    public UpdateVehicleEvent(VehicleEventData eventData) {
        super();
        this.eventData = eventData;
    }
}
