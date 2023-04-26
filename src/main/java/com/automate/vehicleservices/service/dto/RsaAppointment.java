package com.automate.vehicleservices.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RsaAppointment {
    private String startTimeSlot;
    private String endTimeSlot;
    private String typeOfService;
    private String address;
    private int customerId;
    private int distance;
    private int serviceAppointmentId;

}
