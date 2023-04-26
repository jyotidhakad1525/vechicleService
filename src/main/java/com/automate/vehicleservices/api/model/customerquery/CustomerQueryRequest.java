package com.automate.vehicleservices.api.model.customerquery;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CustomerQueryRequest {

    private String actionPlan;

    private String assignedTo;

    private String createdBy;

    private String crmRemarks;

    private String enquiryDepartment;

    private String purpose;

    private String query;

    private String vehicleRegNumber;

    private int customerId;

    private String status;

}