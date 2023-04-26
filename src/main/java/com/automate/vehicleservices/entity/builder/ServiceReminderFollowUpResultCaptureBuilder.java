package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpResultCapture;

import java.time.LocalDateTime;

public final class ServiceReminderFollowUpResultCaptureBuilder {
    private int id;
    private ServiceReminderFollowUpActivity serviceReminderFollowUpActivity;
    private String resultJsonData;
    private String createdBy;
    private LocalDateTime createdDate;

    private ServiceReminderFollowUpResultCaptureBuilder() {
    }

    public static ServiceReminderFollowUpResultCaptureBuilder aServiceReminderFollowUpResultCapture() {
        return new ServiceReminderFollowUpResultCaptureBuilder();
    }

    public ServiceReminderFollowUpResultCaptureBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceReminderFollowUpResultCaptureBuilder withServiceReminderFollowUpActivity(
            ServiceReminderFollowUpActivity serviceReminderFollowUpActivity) {
        this.serviceReminderFollowUpActivity = serviceReminderFollowUpActivity;
        return this;
    }

    public ServiceReminderFollowUpResultCaptureBuilder withResultJsonData(String resultJsonData) {
        this.resultJsonData = resultJsonData;
        return this;
    }

    public ServiceReminderFollowUpResultCaptureBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceReminderFollowUpResultCaptureBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceReminderFollowUpResultCapture build() {
        ServiceReminderFollowUpResultCapture serviceReminderFollowUpResultCapture =
                new ServiceReminderFollowUpResultCapture();
        serviceReminderFollowUpResultCapture.setId(id);
        serviceReminderFollowUpResultCapture.setServiceReminderFollowUpActivity(serviceReminderFollowUpActivity);
        serviceReminderFollowUpResultCapture.setResultJsonData(resultJsonData);
        serviceReminderFollowUpResultCapture.setCreatedBy(createdBy);
        serviceReminderFollowUpResultCapture.setCreatedDate(createdDate);
        return serviceReminderFollowUpResultCapture;
    }
}
