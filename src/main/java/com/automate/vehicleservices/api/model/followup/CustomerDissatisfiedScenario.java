package com.automate.vehicleservices.api.model.followup;

import com.automate.vehicleservices.api.model.CustomerDissatisfiedRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDissatisfiedScenario extends FollowUpResultCapture {

    @NotNull
    private CustomerDissatisfiedRequest customerDissatisfiedRequest;

}
