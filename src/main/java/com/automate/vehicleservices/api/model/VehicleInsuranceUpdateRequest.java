package com.automate.vehicleservices.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleInsuranceUpdateRequest {
    private double insuranceAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String vendor;
    private String insuranceIdentifier;
}
