package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class CustomerVehiclesDTO {

    private CustomerDTO customer;

    private List<ServiceVehicleDTO> vehicles = new ArrayList<>();

    public CustomerVehiclesDTO(CustomerDTO customer, List<ServiceVehicleDTO> vehicles) {
        this.customer = customer;
        this.vehicles = vehicles;
    }
}
