package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import com.automate.vehicleservices.service.dto.ServiceAppointmentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentBookedEvent extends Event {
    private ServiceAppointmentResponse serviceAppointmentResponse;

}
