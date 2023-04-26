package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.CommunicationMode;
import com.automate.vehicleservices.entity.ServiceReminder;
import com.automate.vehicleservices.entity.ServiceReminderDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ServiceReminderDetailsBuilder {
    private int id;
    private String communicationAddress;
    private LocalDate dateOfReminder;
    private CommunicationMode communicationMode;
    private String messageBody;
    private ServiceReminder serviceReminder;
    private String remarks;
    private boolean success;
    private boolean active;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private ServiceReminderDetailsBuilder() {
    }

    public static ServiceReminderDetailsBuilder aServiceReminderDetails() {
        return new ServiceReminderDetailsBuilder();
    }

    public ServiceReminderDetailsBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceReminderDetailsBuilder withCommunicationAddress(String communicationAddress) {
        this.communicationAddress = communicationAddress;
        return this;
    }

    public ServiceReminderDetailsBuilder withDateOfReminder(LocalDate dateOfReminder) {
        this.dateOfReminder = dateOfReminder;
        return this;
    }

    public ServiceReminderDetailsBuilder withCommunicationMode(CommunicationMode communicationMode) {
        this.communicationMode = communicationMode;
        return this;
    }

    public ServiceReminderDetailsBuilder withMessageBody(String messageBody) {
        this.messageBody = messageBody;
        return this;
    }

    public ServiceReminderDetailsBuilder withServiceReminder(ServiceReminder serviceReminder) {
        this.serviceReminder = serviceReminder;
        return this;
    }

    public ServiceReminderDetailsBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public ServiceReminderDetailsBuilder withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ServiceReminderDetailsBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public ServiceReminderDetailsBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceReminderDetailsBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceReminderDetailsBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceReminderDetailsBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceReminderDetailsBuilder but() {
        return aServiceReminderDetails().withId(id).withCommunicationAddress(communicationAddress)
                .withDateOfReminder(dateOfReminder).withCommunicationMode(communicationMode)
                .withMessageBody(messageBody).withServiceReminder(serviceReminder).withRemarks(remarks)
                .withSuccess(success).withActive(active).withCreatedBy(createdBy).withCreatedDate(createdDate)
                .withLastModifiedBy(lastModifiedBy).withLastModifiedDate(lastModifiedDate);
    }

    public ServiceReminderDetails build() {
        ServiceReminderDetails serviceReminderDetails = new ServiceReminderDetails();
        serviceReminderDetails.setId(id);
        serviceReminderDetails.setCommunicationAddress(communicationAddress);
        serviceReminderDetails.setDateOfReminder(dateOfReminder);
        serviceReminderDetails.setCommunicationMode(communicationMode);
        serviceReminderDetails.setMessageBody(messageBody);
        serviceReminderDetails.setServiceReminder(serviceReminder);
        serviceReminderDetails.setRemarks(remarks);
        serviceReminderDetails.setSuccess(success);
        serviceReminderDetails.setActive(active);
        serviceReminderDetails.setCreatedBy(createdBy);
        serviceReminderDetails.setCreatedDate(createdDate);
        serviceReminderDetails.setLastModifiedBy(lastModifiedBy);
        serviceReminderDetails.setLastModifiedDate(lastModifiedDate);
        return serviceReminderDetails;
    }
}
