package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleWarranty;
import com.automate.vehicleservices.entity.enums.WarrantyStatus;
import com.automate.vehicleservices.entity.enums.WarrantyType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class VehicleWarrantyBuilder {
    private int id;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private WarrantyStatus status;
    private double amountPaid;
    private WarrantyType warrantyTpe;
    private String oemPeriod;
    private String ewName;
    private String amc_name;
    private String fastagStatus;
    private MdTenant mdTenant;
    private ServiceVehicle serviceVehicle;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private String number;

    private VehicleWarrantyBuilder() {
    }

    public static VehicleWarrantyBuilder aVehicleWarranty() {
        return new VehicleWarrantyBuilder();
    }

    public VehicleWarrantyBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public VehicleWarrantyBuilder withNumber(String number) {
        this.number = number;
        return this;
    }

    public VehicleWarrantyBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public VehicleWarrantyBuilder withExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public VehicleWarrantyBuilder withStatus(WarrantyStatus status) {
        this.status = status;
        return this;
    }

    public VehicleWarrantyBuilder withAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
        return this;
    }

    public VehicleWarrantyBuilder withWarrantyTpe(WarrantyType warrantyTpe) {
        this.warrantyTpe = warrantyTpe;
        return this;
    }

    public VehicleWarrantyBuilder withOemPeriod(String oemPeriod) {
        this.oemPeriod = oemPeriod;
        return this;
    }

    public VehicleWarrantyBuilder withEwName(String ewName) {
        this.ewName = ewName;
        return this;
    }

    public VehicleWarrantyBuilder withAmcName(String amc_name) {
        this.amc_name = amc_name;
        return this;
    }

    public VehicleWarrantyBuilder withFastagStatus(String fastagStatus) {
        this.fastagStatus = fastagStatus;
        return this;
    }

    public VehicleWarrantyBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public VehicleWarrantyBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public VehicleWarrantyBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public VehicleWarrantyBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public VehicleWarrantyBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public VehicleWarrantyBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public VehicleWarranty build() {
        VehicleWarranty vehicleWarranty = new VehicleWarranty();
        vehicleWarranty.setId(id);
        vehicleWarranty.setStartDate(startDate);
        vehicleWarranty.setExpiryDate(expiryDate);
        vehicleWarranty.setStatus(status);
        vehicleWarranty.setAmountPaid(amountPaid);
        vehicleWarranty.setWarrantyTpe(warrantyTpe);
        vehicleWarranty.setMdTenant(mdTenant);
        vehicleWarranty.setServiceVehicle(serviceVehicle);
        vehicleWarranty.setCreatedBy(createdBy);
        vehicleWarranty.setCreatedDate(createdDate);
        vehicleWarranty.setLastModifiedBy(lastModifiedBy);
        vehicleWarranty.setLastModifiedDate(lastModifiedDate);
        vehicleWarranty.setAmc_name(amc_name);
        vehicleWarranty.setEwName(ewName);
        vehicleWarranty.setFastagStatus(fastagStatus);
        vehicleWarranty.setOemPeriod(oemPeriod);
        vehicleWarranty.setNumber(number);
        return vehicleWarranty;
    }
}
