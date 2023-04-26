package com.automate.vehicleservices.api.model.followup;

import com.automate.vehicleservices.api.model.VehicleServiceHistoryRequest;
import com.automate.vehicleservices.entity.enums.ServicedAt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlreadyServicedScenario extends FollowUpResultCapture {

    @NotNull
    private ServicedAt servicedAt;
    private String nameOfTheDealerOrWorkshop;

    private VehicleServiceHistoryRequest vehicleServiceHistoryRequest;
}
