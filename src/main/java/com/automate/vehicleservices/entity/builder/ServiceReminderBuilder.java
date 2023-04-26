package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.enums.ReminderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ServiceReminderBuilder {
    private boolean active;
    private LocalDate remindUntil;
    private LocalDate reminderStartDate;
    private ServiceVehicle serviceVehicle;
    private MdServiceType mdServiceType;
    private VehicleServiceSchedule vehicleServiceSchedule;
    private List<ServiceReminderDetails> serviceReminderDetails = new ArrayList<>();
    private MdTenant mdTenant;
    private ReminderStatus reminderStatus;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private ServiceReminderBuilder() {
    }

    public static ServiceReminderBuilder aServiceReminder() {
        return new ServiceReminderBuilder();
    }

    public ServiceReminderBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public ServiceReminderBuilder withRemindUntil(LocalDate remindUntil) {
        this.remindUntil = remindUntil;
        return this;
    }

    public ServiceReminderBuilder withReminderStartDate(LocalDate reminderStartDate) {
        this.reminderStartDate = reminderStartDate;
        return this;
    }

    public ServiceReminderBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public ServiceReminderBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }

    public ServiceReminderBuilder withVehicleServiceSchedule(VehicleServiceSchedule vehicleServiceSchedule) {
        this.vehicleServiceSchedule = vehicleServiceSchedule;
        return this;
    }

    public ServiceReminderBuilder withServiceReminderDetails(List<ServiceReminderDetails> serviceReminderDetails) {
        this.serviceReminderDetails = serviceReminderDetails;
        return this;
    }

    public ServiceReminderBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public ServiceReminderBuilder withReminderStatus(ReminderStatus reminderStatus) {
        this.reminderStatus = reminderStatus;
        return this;
    }

    public ServiceReminderBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceReminderBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceReminderBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceReminderBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceReminder build() {
        ServiceReminder serviceReminder = new ServiceReminder();
        serviceReminder.setActive(active);
        serviceReminder.setRemindUntil(remindUntil);
        serviceReminder.setReminderStartDate(reminderStartDate);
        serviceReminder.setServiceVehicle(serviceVehicle);
        serviceReminder.setMdServiceType(mdServiceType);
        serviceReminder.setVehicleServiceSchedule(vehicleServiceSchedule);
        serviceReminder.setServiceReminderDetails(serviceReminderDetails);
        serviceReminder.setMdTenant(mdTenant);
        serviceReminder.setReminderStatus(reminderStatus);
        serviceReminder.setCreatedBy(createdBy);
        serviceReminder.setCreatedDate(createdDate);
        serviceReminder.setLastModifiedBy(lastModifiedBy);
        serviceReminder.setLastModifiedDate(lastModifiedDate);
        return serviceReminder;
    }
}
