package com.automate.vehicleservices.api.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CallAuditResponse {
	private Integer id;
	private String location;
	private String branch;
	private String customerName;
	private String mobileNumber;
	private String regNumber;
	private String serviceType;
	private String subServiceType;
	private String creName;
	private LocalDateTime callDateTime;
	private FollowUpActivityResult feedback;
	private String callDuration;
	private String recording;

	public CallAuditResponse(CallAuditResponse l) {
		if (Objects.isNull(l))
			return;
		this.id = l.id;
		this.serviceType = l.serviceType;
		this.subServiceType = l.subServiceType;
		this.location = l.location;
		this.branch = l.branch;
		this.customerName = l.customerName;
		this.mobileNumber = l.mobileNumber;
		this.regNumber = l.regNumber;
		this.creName = l.creName;
		this.callDateTime = l.callDateTime;
		this.feedback = l.feedback;
		this.callDuration = l.callDuration;
		this.recording = l.recording;
	}
}
