package com.automate.vehicleservices.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VehicleInsuranceRequest {

    private Integer id;
    private Double insuranceAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String vendor;
    private String insuranceIdentifier;
    private CustomerRequest customerRequest;
}
