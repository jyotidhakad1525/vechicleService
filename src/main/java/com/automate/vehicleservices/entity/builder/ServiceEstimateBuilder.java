package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.api.model.PaymentType;
import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.enums.ServiceGroup;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ServiceEstimateBuilder {
    private int id;
    private ServiceGroup serviceGroup;
    private MdTenant mdTenant;
    //bi-directional many-to-one association to ServiceAppointmentAdditionalService
    private List<ServiceEstimateAdditionalService> serviceEstimateAdditionalServices;
    private Set<ServiceDocument> serviceDocuments = new HashSet<>();
    //bi-directional many-to-one association to Customer
    private Customer customer;
    //bi-directional many-to-one association to VehicleKmTracker
    private VehicleKmTracker vehicleKmTracker;
    //bi-directional many-to-one association to ServiceVehicle
    private ServiceVehicle serviceVehicle;
    //bi-directional one-to-one association to ServiceEstimateResponse
    private ServiceEstimateResponse serviceEstimateResponse;
    private PaymentType paymentType;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private ServiceEstimateBuilder() {
    }

    public static ServiceEstimateBuilder aServiceEstimate() {
        return new ServiceEstimateBuilder();
    }

    public ServiceEstimateBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceEstimateBuilder withServiceGroup(ServiceGroup serviceGroup) {
        this.serviceGroup = serviceGroup;
        return this;
    }

    public ServiceEstimateBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public ServiceEstimateBuilder withServiceEstimateAdditionalServices(List<ServiceEstimateAdditionalService> serviceEstimateAdditionalServices) {
        this.serviceEstimateAdditionalServices = serviceEstimateAdditionalServices;
        return this;
    }

    public ServiceEstimateBuilder withServiceDocuments(Set<ServiceDocument> serviceDocuments) {
        this.serviceDocuments = serviceDocuments;
        return this;
    }

    public ServiceEstimateBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public ServiceEstimateBuilder withVehicleKmTracker(VehicleKmTracker vehicleKmTracker) {
        this.vehicleKmTracker = vehicleKmTracker;
        return this;
    }

    public ServiceEstimateBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public ServiceEstimateBuilder withServiceEstimateResponse(ServiceEstimateResponse serviceEstimateResponse) {
        this.serviceEstimateResponse = serviceEstimateResponse;
        return this;
    }

    public ServiceEstimateBuilder withPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public ServiceEstimateBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceEstimateBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceEstimateBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceEstimateBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceEstimate build() {
        ServiceEstimate serviceEstimate = new ServiceEstimate();
        serviceEstimate.setId(id);
        serviceEstimate.setServiceGroup(serviceGroup);
        serviceEstimate.setMdTenant(mdTenant);
        serviceEstimate.setServiceEstimateAdditionalServices(serviceEstimateAdditionalServices);
        serviceEstimate.setServiceDocuments(serviceDocuments);
        serviceEstimate.setCustomer(customer);
        serviceEstimate.setVehicleKmTracker(vehicleKmTracker);
        serviceEstimate.setServiceVehicle(serviceVehicle);
        serviceEstimate.setServiceEstimateResponse(serviceEstimateResponse);
        serviceEstimate.setPaymentType(paymentType);
        serviceEstimate.setCreatedBy(createdBy);
        serviceEstimate.setCreatedDate(createdDate);
        serviceEstimate.setLastModifiedBy(lastModifiedBy);
        serviceEstimate.setLastModifiedDate(lastModifiedDate);
        return serviceEstimate;
    }
}
