package com.automate.vehicleservices.api.model;

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
public class LeadTransferFilterrequest {
	private String location;
	private String serviceCenterCode;
	private Integer empId;
	private String serviceType;

}
