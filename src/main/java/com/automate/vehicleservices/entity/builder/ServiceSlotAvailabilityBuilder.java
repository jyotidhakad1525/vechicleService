package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceSlot;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceSlotAvailability;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Chandrashekar V
 */
public final class ServiceSlotAvailabilityBuilder {
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    private LocalDate serviceDate;
    //uni-directional many-to-one association to MdServiceSlot
    private MdServiceSlot mdServiceSlot;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;

    private boolean isActive;

    private ServiceSlotAvailabilityBuilder() {
    }

    public static ServiceSlotAvailabilityBuilder aServiceSlotAvailability() {
        return new ServiceSlotAvailabilityBuilder();
    }

    public ServiceSlotAvailabilityBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceSlotAvailabilityBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceSlotAvailabilityBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceSlotAvailabilityBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceSlotAvailabilityBuilder withServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
        return this;
    }

    public ServiceSlotAvailabilityBuilder withMdServiceSlot(MdServiceSlot mdServiceSlot) {
        this.mdServiceSlot = mdServiceSlot;
        return this;
    }

    public ServiceSlotAvailabilityBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public ServiceSlotAvailabilityBuilder withIsActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public ServiceSlotAvailabilityBuilder but() {
        return aServiceSlotAvailability().withCreatedBy(createdBy).withCreatedDate(createdDate)
                .withLastModifiedBy(lastModifiedBy).withLastModifiedDate(lastModifiedDate).withServiceDate(serviceDate)
                .withMdServiceSlot(mdServiceSlot).withMdTenant(mdTenant).withIsActive(isActive);
    }

    public ServiceSlotAvailability build() {
        ServiceSlotAvailability serviceSlotAvailability = new ServiceSlotAvailability();
        serviceSlotAvailability.setCreatedBy(createdBy);
        serviceSlotAvailability.setCreatedDate(createdDate);
        serviceSlotAvailability.setLastModifiedBy(lastModifiedBy);
        serviceSlotAvailability.setLastModifiedDate(lastModifiedDate);
        serviceSlotAvailability.setServiceDate(serviceDate);
        serviceSlotAvailability.setMdServiceSlot(mdServiceSlot);
        serviceSlotAvailability.setMdTenant(mdTenant);
        serviceSlotAvailability.setActive(isActive);
        return serviceSlotAvailability;
    }
}
