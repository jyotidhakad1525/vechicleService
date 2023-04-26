package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.RSA;
import com.automate.vehicleservices.entity.RSAAddress;
import com.automate.vehicleservices.entity.ServiceVehicle;

import java.time.LocalDateTime;
import java.util.Date;

public final class RSABuilder {
    private int id;
    private double amount;
    private int customerId;
    private Date date;
    private String reason;
    private String remarks;
    private String status;
    private RSAAddress rsaAddress;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private String branchName;
    private Employee employee;
    private ServiceVehicle serviceVehicle;

    private RSABuilder() {
    }

    public static RSABuilder aRSA() {
        return new RSABuilder();
    }

    public RSABuilder withId(int id) {
        this.id = id;
        return this;
    }

    public RSABuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public RSABuilder withCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public RSABuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public RSABuilder withReason(String reason) {
        this.reason = reason;
        return this;
    }

    public RSABuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public RSABuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public RSABuilder withRsaAddress(RSAAddress rsaAddress) {
        this.rsaAddress = rsaAddress;
        return this;
    }

    public RSABuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public RSABuilder withCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public RSABuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public RSABuilder withLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public RSABuilder withBranchName(String branchName) {
        this.branchName = branchName;
        return this;
    }
    public RSABuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public RSABuilder withTechnician(Employee employee) {
        this.employee = employee;
        return this;
    }

    public RSA build() {
        RSA rSA = new RSA();
        rSA.setId(id);
        rSA.setAmount(amount);
        rSA.setCustomerId(customerId);
        rSA.setDate(date);
        rSA.setReason(reason);
        rSA.setRemarks(remarks);
        rSA.setStatus(status);
        rSA.setRsaAddress(rsaAddress);
        rSA.setCreatedBy(createdBy);
        rSA.setCreatedDate(createdDate);
        rSA.setLastModifiedBy(lastModifiedBy);
        rSA.setLastModifiedDate(lastModifiedDate);
        rSA.setBranchName(branchName);
        rSA.setTechnician(employee);
        rSA.setVehicle(serviceVehicle);
        return rSA;
    }
}
