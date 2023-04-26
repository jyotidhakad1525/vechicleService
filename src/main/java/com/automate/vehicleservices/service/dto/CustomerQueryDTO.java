package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.CustomerQuery;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryDepartment;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryPurpose;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CustomerQueryDTO {
    private String id;
    private int customerId;
    private String creAssigned;
    private String actionPlan;
    private String creRemarks;
    private String query;
    private String vehicleRegNumber;
    private CustomerQueryEnquiryDepartment customerQueryEnquiryDepartment;
    private CustomerQueryEnquiryStatus customerQueryEnquiryStatus;
    private CustomerQueryEnquiryPurpose customerQueryEnquiryPurpose;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public CustomerQueryDTO(CustomerQuery customerQuery) {
        this.id = customerQuery.getId();
        this.customerId = customerQuery.getCustomer().getId();
        this.creAssigned = customerQuery.getAssignedTo();
        this.actionPlan = customerQuery.getActionPlan();
        this.creRemarks = customerQuery.getCreRemarks();
        this.query = customerQuery.getQuery();
        this.vehicleRegNumber = customerQuery.getServiceVehicle().getRegNumber();
        this.customerQueryEnquiryDepartment = customerQuery.getCustomerQueryEnquiryDepartment();
        this.customerQueryEnquiryStatus = customerQuery.getCustomerQueryEnquiryStatus();
        this.customerQueryEnquiryPurpose = customerQuery.getCustomerQueryEnquiryPurpose();
        this.createdBy = customerQuery.getCreatedBy();
        this.createdDate = customerQuery.getCreatedDate();
        this.lastModifiedBy = customerQuery.getLastModifiedBy();
        this.lastModifiedDate = customerQuery.getLastModifiedDate();
    }
}
