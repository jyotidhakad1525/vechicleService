package com.automate.vehicleservices.api.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Shubham r
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class CallAuditFilterModel {

	// same as complaint traker screen start date and enddate
	private LocalDate startDate;
	private LocalDate endDate;
	private String serviceCenterLocation;
	private String subCenterBranch;
	private String otherColumn;
}
