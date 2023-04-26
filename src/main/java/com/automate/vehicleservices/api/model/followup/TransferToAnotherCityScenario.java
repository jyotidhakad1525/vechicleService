package com.automate.vehicleservices.api.model.followup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferToAnotherCityScenario extends FollowUpResultCapture {
    @NotNull
    private String nameOfTheLocation;
    private String creRemarks;
}
