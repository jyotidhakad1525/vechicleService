package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.WarrantyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleWarrantyRequest {
    private Integer id;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private Double amountPaid;
    private String oemPeriod;
    private String ewName;
    private String amc_name;
    private String fastagStatus;
    private WarrantyType warrantyType;
    private String number;
}
