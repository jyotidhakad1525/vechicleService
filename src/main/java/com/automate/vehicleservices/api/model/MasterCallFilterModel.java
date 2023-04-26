package com.automate.vehicleservices.api.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class MasterCallFilterModel {
	// same as complaint traker screen start date and enddate
	private LocalDate startDueDate;
	private LocalDate endDueDate;
	private String location;
	private String serviceCenterCode;
	private String serviceType;
	private String model;
}
