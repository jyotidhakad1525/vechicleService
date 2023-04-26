package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.repository.dtoprojection.VehicleServiceScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ConsolidateCustomerVehicleResponse {
    private CustomerDTO customer;
    private ServiceVehicleDTO vehicle;
    private List<VehicleWarrantyDTO> vehicleWarranty;
    private VehicleInsuranceMinimalDTO vehicleInsurance;
    private VehicleServiceScheduleDto vehicleServiceScheduleDto;
}
