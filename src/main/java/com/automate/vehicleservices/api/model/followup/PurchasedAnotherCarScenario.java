package com.automate.vehicleservices.api.model.followup;

import com.automate.vehicleservices.api.model.VehicleDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedAnotherCarScenario extends FollowUpResultCapture {

    @NotNull
    private VehicleDetails vehicleDetails;
}
