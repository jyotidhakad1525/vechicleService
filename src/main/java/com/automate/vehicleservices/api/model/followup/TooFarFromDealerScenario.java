package com.automate.vehicleservices.api.model.followup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TooFarFromDealerScenario extends FollowUpResultCapture {
    private String newAreaPinCode;
    private boolean preferredDoorStepService;
}
