package com.automate.vehicleservices.event;


import com.automate.vehicleservices.framework.event.Event;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CancelServiceReminderEvent extends Event {
    private VehicleEventData vehicleEventData;
    private List<Integer> reminderIds;

    @Builder
    public CancelServiceReminderEvent(VehicleEventData eventData, final List<Integer> reminderIds) {
        super();
        this.reminderIds = reminderIds;
        this.vehicleEventData = eventData;
    }
}
