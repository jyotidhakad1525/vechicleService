package com.automate.vehicleservices.api.model;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;


@Data
public class VehicleInsuranceDetails {
    private double insuranceAmount;
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;
    @NotBlank
    private String vendor;
    @NotBlank
    private String policyNumber;
}
