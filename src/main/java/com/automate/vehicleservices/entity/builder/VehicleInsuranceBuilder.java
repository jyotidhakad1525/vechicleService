package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.ServiceDocument;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleInsurance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public final class VehicleInsuranceBuilder {
    private int id;
    private String insuranceIdentifier;
    private String provider;
    private double insuranceAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private ServiceVehicle serviceVehicle;
    private Customer customer;
    private Set<ServiceDocument> serviceDocuments = new HashSet<>();
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private VehicleInsuranceBuilder() {
    }

    public static VehicleInsuranceBuilder aVehicleInsurance() {
        return new VehicleInsuranceBuilder();
    }

    public VehicleInsuranceBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public VehicleInsuranceBuilder withInsuranceIdentifier(String insuranceIdentifier) {
        this.insuranceIdentifier = insuranceIdentifier;
        return this;
    }

    public VehicleInsuranceBuilder withProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public VehicleInsuranceBuilder withInsuranceAmount(double insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
        return this;
    }

    public VehicleInsuranceBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public VehicleInsuranceBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public VehicleInsuranceBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public VehicleInsuranceBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public VehicleInsuranceBuilder withServiceDocuments(Set<ServiceDocument> serviceDocuments) {
        this.serviceDocuments = serviceDocuments;
        return this;
    }

    public VehicleInsuranceBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public VehicleInsuranceBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public VehicleInsuranceBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public VehicleInsuranceBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public VehicleInsurance build() {
        VehicleInsurance vehicleInsurance = new VehicleInsurance();
        vehicleInsurance.setId(id);
        vehicleInsurance.setInsuranceIdentifier(insuranceIdentifier);
        vehicleInsurance.setProvider(provider);
        vehicleInsurance.setInsuranceAmount(insuranceAmount);
        vehicleInsurance.setStartDate(startDate);
        vehicleInsurance.setEndDate(endDate);
        vehicleInsurance.setServiceVehicle(serviceVehicle);
        vehicleInsurance.setCustomer(customer);
        vehicleInsurance.setServiceDocuments(serviceDocuments);
        vehicleInsurance.setCreatedBy(createdBy);
        vehicleInsurance.setCreatedDate(createdDate);
        vehicleInsurance.setLastModifiedBy(lastModifiedBy);
        vehicleInsurance.setLastModifiedDate(lastModifiedDate);
        return vehicleInsurance;
    }
}
