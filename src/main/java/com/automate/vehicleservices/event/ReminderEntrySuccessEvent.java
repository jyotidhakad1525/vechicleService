package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import com.automate.vehicleservices.service.dto.ServiceReminderDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReminderEntrySuccessEvent extends Event {
    private ServiceReminderDTO serviceReminderDTO;

    @Builder
    public ReminderEntrySuccessEvent(ServiceReminderDTO eventData) {
        super();
        this.serviceReminderDTO = eventData;
    }
}
