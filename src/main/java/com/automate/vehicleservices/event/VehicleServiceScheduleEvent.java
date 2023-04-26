package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VehicleServiceScheduleEvent extends Event {
    protected String vehicleRegNumber;
    protected int jobId;

    public VehicleServiceScheduleEvent(String vehicleRegNumber, int jobId) {
        super();
        this.vehicleRegNumber = vehicleRegNumber;
        this.jobId = jobId;
    }
}
