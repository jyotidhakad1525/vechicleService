package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.Gender;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Shubham r
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class LeadAllocationFilterRequest {

	// same as complaint traker screen start date and enddate
	private LocalDate startDueDate;
	private LocalDate endDueDate;
	private String serviceType;
	private String subServiceType;
	private String model;

}
