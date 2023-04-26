package com.automate.vehicleservices.event;


import com.automate.vehicleservices.framework.event.Event;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CancelServiceScheduleEvent extends Event {
    private VehicleEventData vehicleEventData;

    @Builder
    public CancelServiceScheduleEvent(VehicleEventData eventData) {
        super();
        this.vehicleEventData = eventData;
    }
}
