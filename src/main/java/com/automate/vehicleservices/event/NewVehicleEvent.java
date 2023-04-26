package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewVehicleEvent extends Event {
    private VehicleEventData eventData;

    @Builder
    public NewVehicleEvent(VehicleEventData eventData) {
        super();
        this.eventData = eventData;
    }


}
