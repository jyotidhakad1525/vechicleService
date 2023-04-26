package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.entity.enums.VehicleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ServiceVehicleBuilder {
    private int id;
    private String additionalDetails;
    private String chassisNumber;
    private String model;
    private LocalDate purchaseDate;
    private String regNumber;
    private String engineNumber;
    private String variant;
    private String color;
    private FuelType fuelType;
    private VehicleStatus status;
    private String vin;
    private String sellingLocation;
    private String sellingDealer;
    private String transmisionType;
    private String vehicleMake;
    private LocalDate vehicleMakeYear;
    
    //bi-directional many-to-one association to Customer
    private Customer customer;
    //bi-directional many-to-one association to VehicleKmTracker
    private List<VehicleKmTracker> vehicleKmTrackers = new ArrayList<>();
    //bi-directional many-to-one association to VehicleKmTracker
    private List<VehicleInsurance> vehicleInsurances = new ArrayList<>();
    private List<VehicleWarranty> vehicleWarranties = new ArrayList<>();
    //uni-directional many-to-one association to MdTenant
    private MdTenant mdTenant;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private Integer currentKmReading;

    private String makingMonth;

    private Integer makingYear;

    private Boolean isFastag;

    private ServiceVehicleBuilder() {
    }

    public static ServiceVehicleBuilder aServiceVehicle() {
        return new ServiceVehicleBuilder();
    }

    public ServiceVehicleBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceVehicleBuilder withIsFastag(Boolean isFastag) {
        this.isFastag = isFastag;
        return this;
    }
    public ServiceVehicleBuilder withMakingMonth(String makingMonth) {
        this.makingMonth = makingMonth;
        return this;
    }

    public ServiceVehicleBuilder withMakingYear(Integer makingYear) {
        this.makingYear = makingYear;
        return this;
    }

    public ServiceVehicleBuilder withCurrentKmReading(Integer currentKmReading) {
        this.currentKmReading = currentKmReading;
        return this;
    }

    public ServiceVehicleBuilder withAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
        return this;
    }

    public ServiceVehicleBuilder withChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
        return this;
    }

    public ServiceVehicleBuilder withModel(String model) {
        this.model = model;
        return this;
    }

    public ServiceVehicleBuilder withPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public ServiceVehicleBuilder withRegNumber(String regNumber) {
        this.regNumber = regNumber;
        return this;
    }

    public ServiceVehicleBuilder withEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
        return this;
    }

    public ServiceVehicleBuilder withVariant(String variant) {
        this.variant = variant;
        return this;
    }

    public ServiceVehicleBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public ServiceVehicleBuilder withFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
        return this;
    }

    public ServiceVehicleBuilder withStatus(VehicleStatus status) {
        this.status = status;
        return this;
    }

    public ServiceVehicleBuilder withVin(String vin) {
        this.vin = vin;
        return this;
    }

    public ServiceVehicleBuilder withSellingLocation(String sellingLocation) {
        this.sellingLocation = sellingLocation;
        return this;
    }

    public ServiceVehicleBuilder withTransmissionType(String transmisionType) {
        this.transmisionType = transmisionType;
        return this;
    }
    
    public ServiceVehicleBuilder withVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
        return this;
    }
    
    public ServiceVehicleBuilder withVehicleMakeYear(LocalDate vehicleMakeYear) {
        this.vehicleMakeYear = vehicleMakeYear;
        return this;
    }
    
    public ServiceVehicleBuilder withSellingDealer(String sellingDealer) {
        this.sellingDealer = sellingDealer;
        return this;
    }

    public ServiceVehicleBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public ServiceVehicleBuilder withVehicleKmTrackers(List<VehicleKmTracker> vehicleKmTrackers) {
        this.vehicleKmTrackers = vehicleKmTrackers;
        return this;
    }

    public ServiceVehicleBuilder withVehicleInsurances(List<VehicleInsurance> vehicleInsurances) {
        this.vehicleInsurances = vehicleInsurances;
        return this;
    }

    public ServiceVehicleBuilder withVehicleWarranties(List<VehicleWarranty> vehicleWarranties) {
        this.vehicleWarranties = vehicleWarranties;
        return this;
    }

    public ServiceVehicleBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public ServiceVehicleBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceVehicleBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceVehicleBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceVehicleBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceVehicle build() {
        ServiceVehicle serviceVehicle = new ServiceVehicle();
        serviceVehicle.setId(id);
        serviceVehicle.setAdditionalDetails(additionalDetails);
        serviceVehicle.setChassisNumber(chassisNumber);
        serviceVehicle.setModel(model);
        serviceVehicle.setPurchaseDate(purchaseDate);
        serviceVehicle.setRegNumber(regNumber);
        serviceVehicle.setEngineNumber(engineNumber);
        serviceVehicle.setVariant(variant);
        serviceVehicle.setColor(color);
        serviceVehicle.setFuelType(fuelType);
        serviceVehicle.setStatus(status);
        serviceVehicle.setVin(vin);
        serviceVehicle.setSellingLocation(sellingLocation);
        serviceVehicle.setSellingDealer(sellingDealer);
        serviceVehicle.setTransmisionType(transmisionType);
        serviceVehicle.setVehicleMake(vehicleMake);
        serviceVehicle.setVehicleMakeYear(vehicleMakeYear);
        serviceVehicle.setCustomer(customer);
        serviceVehicle.setVehicleKmTrackers(vehicleKmTrackers);
        serviceVehicle.setVehicleInsurances(vehicleInsurances);
        serviceVehicle.setVehicleWarranties(vehicleWarranties);
        serviceVehicle.setMdTenant(mdTenant);
        serviceVehicle.setCreatedBy(createdBy);
        serviceVehicle.setCreatedDate(createdDate);
        serviceVehicle.setLastModifiedBy(lastModifiedBy);
        serviceVehicle.setLastModifiedDate(lastModifiedDate);
        serviceVehicle.setCurrentKmReading(currentKmReading);
        serviceVehicle.setMakingMonth(makingMonth);
        serviceVehicle.setMakingYear(makingYear);
        serviceVehicle.setIsFastag(isFastag);
        return serviceVehicle;
    }
}
