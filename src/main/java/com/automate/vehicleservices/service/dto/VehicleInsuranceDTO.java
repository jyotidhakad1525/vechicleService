package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.VehicleInsurance;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleInsuranceDTO {
    private int id;
    private String provider;
    private double insuranceAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private ServiceVehicleDTO serviceVehicle;
    private CustomerDTO customerDTO;

    public VehicleInsuranceDTO(VehicleInsurance vehicleInsurance) {
        if (vehicleInsurance == null)
            return;

        this.id = vehicleInsurance.getId();
        this.provider = vehicleInsurance.getProvider();
        this.insuranceAmount = vehicleInsurance.getInsuranceAmount();
        this.startDate = vehicleInsurance.getStartDate();
        this.endDate = vehicleInsurance.getEndDate();
        this.serviceVehicle = new ServiceVehicleDTO(vehicleInsurance.getServiceVehicle());
        this.customerDTO = new CustomerDTO(vehicleInsurance.getCustomer());
    }
}
