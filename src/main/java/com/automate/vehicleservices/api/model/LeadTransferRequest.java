package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.Gender;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * @author shubham r
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class LeadTransferRequest {
	// employee id of assigned employee
	private Integer employeeId;
	// serviceCenterCode id of assigned employee
	private String serviceCenterCode;
	// location id of assigned employee
	private String location;
	// selected leadIds
	private List<Integer> followUpId;
}
