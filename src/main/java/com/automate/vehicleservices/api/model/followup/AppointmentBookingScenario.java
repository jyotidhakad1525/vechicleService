package com.automate.vehicleservices.api.model.followup;

import com.automate.vehicleservices.api.model.AppointmentRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentBookingScenario extends FollowUpResultCapture {

    @NotNull
    private AppointmentRequest appointmentRequest;

    @JsonIgnore
    public boolean isAsync() {
        return false;
    }
}
