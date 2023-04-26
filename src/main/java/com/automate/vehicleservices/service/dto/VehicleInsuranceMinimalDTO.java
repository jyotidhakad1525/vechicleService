package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.VehicleInsurance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleInsuranceMinimalDTO {
    private int id;
    private String insuranceIdentifier;
    private String provider;
    private double insuranceAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String vehicleRegNumber;
    private String customer;

    public VehicleInsuranceMinimalDTO(VehicleInsurance vehicleInsurance) {
        if (Objects.isNull(vehicleInsurance)) {
            new VehicleInsuranceMinimalDTO();
            return;
        }

        this.insuranceIdentifier = vehicleInsurance.getInsuranceIdentifier();
        this.id = vehicleInsurance.getId();
        this.provider = vehicleInsurance.getProvider();
        this.insuranceAmount = Optional.ofNullable(vehicleInsurance.getInsuranceAmount()).orElse(0d);
        this.startDate = vehicleInsurance.getStartDate();
        this.endDate = vehicleInsurance.getEndDate();
        final var serviceVehicle = vehicleInsurance.getServiceVehicle();
        if (Objects.nonNull(serviceVehicle))
            this.vehicleRegNumber = serviceVehicle.getRegNumber();

        final var customer = vehicleInsurance.getCustomer();
        if (Objects.nonNull(customer))
            this.customer = customer.getFullName();
    }
}
