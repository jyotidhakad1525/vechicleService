package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SchedulingSuccessEvent extends Event {
    private ServiceScheduleDTO eventData;

    @Builder
    public SchedulingSuccessEvent(ServiceScheduleDTO eventData) {
        super();
        this.eventData = eventData;
    }


}
