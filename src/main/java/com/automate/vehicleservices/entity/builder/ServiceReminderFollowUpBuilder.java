package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.enums.FollowUpStatus;

import java.time.LocalDateTime;
import java.util.List;

public final class ServiceReminderFollowUpBuilder {
    private int id;
    private FollowUpStatus status;
    private ServiceReminderDetails serviceReminderDetails;
    private Customer customer;
    private Employee cre;
    private List<ServiceReminderFollowUpActivity> serviceFollowUpActivities;
    private ServiceVehicle serviceVehicle;
    private MdTenant tenant;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private ServiceReminderFollowUpBuilder() {
    }

    public static ServiceReminderFollowUpBuilder aServiceReminderFollowUp() {
        return new ServiceReminderFollowUpBuilder();
    }

    public ServiceReminderFollowUpBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceReminderFollowUpBuilder withStatus(FollowUpStatus status) {
        this.status = status;
        return this;
    }

    public ServiceReminderFollowUpBuilder withServiceReminderDetails(ServiceReminderDetails serviceReminderDetails) {
        this.serviceReminderDetails = serviceReminderDetails;
        return this;
    }

    public ServiceReminderFollowUpBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public ServiceReminderFollowUpBuilder withCre(Employee cre) {
        this.cre = cre;
        return this;
    }

    public ServiceReminderFollowUpBuilder withServiceFollowUpActivities(
            List<ServiceReminderFollowUpActivity> serviceFollowUpActivities) {
        this.serviceFollowUpActivities = serviceFollowUpActivities;
        return this;
    }

    public ServiceReminderFollowUpBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public ServiceReminderFollowUpBuilder withTenant(MdTenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public ServiceReminderFollowUpBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceReminderFollowUpBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceReminderFollowUpBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceReminderFollowUpBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceReminderFollowUp build() {
        ServiceReminderFollowUp serviceReminderFollowUp = new ServiceReminderFollowUp();
        serviceReminderFollowUp.setId(id);
        serviceReminderFollowUp.setStatus(status);
        serviceReminderFollowUp.setServiceReminderDetails(serviceReminderDetails);
        serviceReminderFollowUp.setCustomer(customer);
        serviceReminderFollowUp.setCre(cre);
        serviceReminderFollowUp.setServiceFollowUpActivities(serviceFollowUpActivities);
        serviceReminderFollowUp.setServiceVehicle(serviceVehicle);
        serviceReminderFollowUp.setTenant(tenant);
        serviceReminderFollowUp.setCreatedBy(createdBy);
        serviceReminderFollowUp.setCreatedDate(createdDate);
        serviceReminderFollowUp.setLastModifiedBy(lastModifiedBy);
        serviceReminderFollowUp.setLastModifiedDate(lastModifiedDate);
        return serviceReminderFollowUp;
    }
}
