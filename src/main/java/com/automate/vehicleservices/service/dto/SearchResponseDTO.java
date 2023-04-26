package com.automate.vehicleservices.service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponseDTO {
	private int customerId;
	private String firstName;
	private String lastName;
	private String vehicleRegNumber;
	private String chassisNumber;
	private String vin;
	private String engineNumber;
	private String policyNumber;
	private String model;
	private String contactNumber;
	private String serviceType;
	private LocalDate dueDate;
	private String subServiceType;
	private String serviceCenterName;
}
