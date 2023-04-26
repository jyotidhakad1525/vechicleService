package com.automate.vehicleservices.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TechnicianDetailsDto {
    List<EmployeeDTO> employeeDTOS;

}
