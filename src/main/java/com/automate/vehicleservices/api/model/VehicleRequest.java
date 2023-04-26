package com.automate.vehicleservices.api.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Chandrashekar V
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleRequest {

    @NotNull
    @Valid
    private VehicleDetails vehicleDetails;

    @NotNull
    @Valid
    private CustomerRequest customer;
}
