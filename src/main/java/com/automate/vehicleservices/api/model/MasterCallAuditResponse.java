package com.automate.vehicleservices.api.model;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MasterCallAuditResponse {
	private Integer id;
	private String location;
	private String serviceCenterCode;
	private String serviceType;
	private LocalDate dueDate;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String pinCode;
	private String model;
	private String vinNumber;
	private String regNumber;
	private String currentMileage;
	private String vehicleSaleDate;
	private String creName;
	private LocalDate lastServiceDate;
	public MasterCallAuditResponse(MasterCallAuditResponse t) {
		if (Objects.isNull(t))
			return;
		this.id = t.id;
		this.location = t.location;
		this.serviceCenterCode = t.serviceCenterCode;
		this.serviceType = t.serviceType;
		this.dueDate = t.dueDate;
		this.firstName = t.firstName;
		this.lastName = t.lastName;
		this.mobileNumber = t.mobileNumber;
		this.pinCode = t.pinCode;
		this.model = t.model;
		this.vinNumber = t.vinNumber;
		this.regNumber = t.regNumber;
		this.currentMileage = t.currentMileage;
		this.vehicleSaleDate = t.vehicleSaleDate;
		this.creName = t.creName;
		this.lastServiceDate = t.lastServiceDate;
	}

	
}
