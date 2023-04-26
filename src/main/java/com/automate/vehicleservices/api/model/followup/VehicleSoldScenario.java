package com.automate.vehicleservices.api.model.followup;

import com.automate.vehicleservices.api.model.CustomerRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleSoldScenario extends FollowUpResultCapture {

    @NotNull
    private CustomerRequest customerRequest;
}
