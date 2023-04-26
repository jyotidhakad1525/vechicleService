package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.ServiceReminderFollowUp;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpResultCapture;
import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;
import com.automate.vehicleservices.entity.enums.FollowUpActivityStatus;
import com.automate.vehicleservices.entity.enums.FollowUpReason;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ServiceReminderFollowUpActivityBuilder {
    private int id;
    private ServiceReminderFollowUp serviceReminderFollowUp;
    private Employee cre;
    private String creRemarks;
    private String customerRemarks;
    private LocalDate startDate;
    private LocalDateTime followUpDate;
    private LocalDate endDate;
    private FollowUpActivityStatus followUpActivityStatus;
    private FollowUpActivityResult followUpActivityResult;
    private FollowUpReason followUpReason;
    private ServiceReminderFollowUpResultCapture serviceReminderFollowUpResultCapture;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private ServiceReminderFollowUpActivityBuilder() {
    }

    public static ServiceReminderFollowUpActivityBuilder aServiceReminderFollowUpActivity() {
        return new ServiceReminderFollowUpActivityBuilder();
    }

    public ServiceReminderFollowUpActivityBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withServiceReminderFollowUp(
            ServiceReminderFollowUp serviceReminderFollowUp) {
        this.serviceReminderFollowUp = serviceReminderFollowUp;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withCre(Employee cre) {
        this.cre = cre;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withCreRemarks(String creRemarks) {
        this.creRemarks = creRemarks;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withCustomerRemarks(String customerRemarks) {
        this.customerRemarks = customerRemarks;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withFollowUpDate(LocalDateTime followUpDate) {
        this.followUpDate = followUpDate;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withFollowUpActivityStatus(
            FollowUpActivityStatus followUpActivityStatus) {
        this.followUpActivityStatus = followUpActivityStatus;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withFollowUpActivityResult(
            FollowUpActivityResult followUpActivityResult) {
        this.followUpActivityResult = followUpActivityResult;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withFollowUpReason(FollowUpReason followUpReason) {
        this.followUpReason = followUpReason;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withServiceReminderFollowUpResultCapture(
            ServiceReminderFollowUpResultCapture serviceReminderFollowUpResultCapture) {
        this.serviceReminderFollowUpResultCapture = serviceReminderFollowUpResultCapture;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceReminderFollowUpActivityBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceReminderFollowUpActivity build() {
        ServiceReminderFollowUpActivity serviceReminderFollowUpActivity = new ServiceReminderFollowUpActivity();
        serviceReminderFollowUpActivity.setId(id);
        serviceReminderFollowUpActivity.setServiceReminderFollowUp(serviceReminderFollowUp);
        serviceReminderFollowUpActivity.setCre(cre);
        serviceReminderFollowUpActivity.setCreRemarks(creRemarks);
        serviceReminderFollowUpActivity.setCustomerRemarks(customerRemarks);
        serviceReminderFollowUpActivity.setStartDate(startDate);
        serviceReminderFollowUpActivity.setFollowUpDate(followUpDate);
        serviceReminderFollowUpActivity.setEndDate(endDate);
        serviceReminderFollowUpActivity.setFollowUpActivityStatus(followUpActivityStatus);
        serviceReminderFollowUpActivity.setFollowUpActivityResult(followUpActivityResult);
        serviceReminderFollowUpActivity.setFollowUpReason(followUpReason);
        serviceReminderFollowUpActivity.setServiceReminderFollowUpResultCapture(serviceReminderFollowUpResultCapture);
        serviceReminderFollowUpActivity.setCreatedBy(createdBy);
        serviceReminderFollowUpActivity.setCreatedDate(createdDate);
        serviceReminderFollowUpActivity.setLastModifiedBy(lastModifiedBy);
        serviceReminderFollowUpActivity.setLastModifiedDate(lastModifiedDate);
        return serviceReminderFollowUpActivity;
    }
}
