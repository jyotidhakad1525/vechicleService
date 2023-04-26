package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.ServiceAppointment;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpResultingAppointment;

import java.time.LocalDateTime;

public final class ServiceReminderFollowUpResultingAppointmentBuilder {
    private int id;
    private ServiceAppointment serviceAppointment;
    private ServiceReminderFollowUpActivity serviceReminderFollowUpActivity;
    private String createdBy;
    private LocalDateTime createdDate;

    private ServiceReminderFollowUpResultingAppointmentBuilder() {
    }

    public static ServiceReminderFollowUpResultingAppointmentBuilder aServiceReminderFollowUpResultingAppointment() {
        return new ServiceReminderFollowUpResultingAppointmentBuilder();
    }

    public ServiceReminderFollowUpResultingAppointmentBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceReminderFollowUpResultingAppointmentBuilder withServiceAppointment(
            ServiceAppointment serviceAppointment) {
        this.serviceAppointment = serviceAppointment;
        return this;
    }

    public ServiceReminderFollowUpResultingAppointmentBuilder withServiceReminderFollowUpActivity(
            ServiceReminderFollowUpActivity serviceReminderFollowUpActivity) {
        this.serviceReminderFollowUpActivity = serviceReminderFollowUpActivity;
        return this;
    }

    public ServiceReminderFollowUpResultingAppointmentBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceReminderFollowUpResultingAppointmentBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceReminderFollowUpResultingAppointment build() {
        ServiceReminderFollowUpResultingAppointment serviceReminderFollowUpResultingAppointment =
                new ServiceReminderFollowUpResultingAppointment();
        serviceReminderFollowUpResultingAppointment.setId(id);
        serviceReminderFollowUpResultingAppointment.setServiceAppointment(serviceAppointment);
        serviceReminderFollowUpResultingAppointment.setServiceReminderFollowUpActivity(serviceReminderFollowUpActivity);
        serviceReminderFollowUpResultingAppointment.setCreatedBy(createdBy);
        serviceReminderFollowUpResultingAppointment.setCreatedDate(createdDate);
        return serviceReminderFollowUpResultingAppointment;
    }
}
