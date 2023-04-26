package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.Employee;
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
public class AllocateLeadToCre {

	private String location;
	private String serviceCenterCode;
	private Integer creId;
	private List<Integer> followUpIds;
}
