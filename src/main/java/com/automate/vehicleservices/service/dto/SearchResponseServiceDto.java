package com.automate.vehicleservices.service.dto;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class SearchResponseServiceDto {
	private int id;
	private String serviceType;
	private String subServiceType;
	private LocalDate dueDate;
	private String firstName;
	private String lastName;
	private String mobileNo;
	private String pincode;
	private String model;
	private String vinNo;
	private String regNo;
	private String currentMileage;
	private LocalDate sellingDate;
	
	public SearchResponseServiceDto(SearchResponseServiceDto l) {
		 if (Objects.isNull(l))
	            return;
		this.id = l.id;
		this.serviceType = l.serviceType;
		this.subServiceType = l.subServiceType;
		this.dueDate = l.dueDate;
		this.firstName = l.firstName;
		this.lastName = l.lastName;
		this.mobileNo = l.mobileNo;
		this.pincode = l.pincode;
		this.model = l.model;
		this.vinNo = l.vinNo;
		this.regNo = l.regNo;
		this.currentMileage = l.currentMileage;
		this.sellingDate = l.sellingDate;
	}
}
