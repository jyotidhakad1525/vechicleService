package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleServiceHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Chandrashekar V
 */
public final class VehicleServiceHistoryBuilder {
    private Integer kmReading;
    private String remarks;
    private float serviceAmount;
    private String serviceCenter;
    private LocalDate serviceDate;
    private String serviceManager;
    private String complaintStatus;
	private String dealerName;
    private String dealerLocation;
    private String lastServiceFeedback;
    private String reasonForComplaint;
    //bi-directional many-to-one association to MdServiceType
    private MdServiceType mdServiceType;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;
    //bi-directional many-to-one association to ServiceVehicle
    private ServiceVehicle serviceVehicle;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private VehicleServiceHistoryBuilder() {
    }

    public static VehicleServiceHistoryBuilder aVehicleServiceHistory() {
        return new VehicleServiceHistoryBuilder();
    }

    public VehicleServiceHistoryBuilder withKmReading(Integer kmReading) {
        this.kmReading = kmReading;
        return this;
    }

    public VehicleServiceHistoryBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public VehicleServiceHistoryBuilder withServiceAmount(float serviceAmount) {
        this.serviceAmount = serviceAmount;
        return this;
    }

    public VehicleServiceHistoryBuilder withServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
        return this;
    }

    public VehicleServiceHistoryBuilder withServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
        return this;
    }

    public VehicleServiceHistoryBuilder withServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
        return this;
    }
    
    public VehicleServiceHistoryBuilder withComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
        return this;
    }
    
    
    public VehicleServiceHistoryBuilder withDealerName(String dealerName) {
        this.dealerName = dealerName;
        return this;
    }
    
    public VehicleServiceHistoryBuilder withDealerLocation(String dealerLocation) {
        this.dealerLocation = dealerLocation;
        return this;
    }
    public VehicleServiceHistoryBuilder withLastServiceFeedback(String lastServiceFeedback) {
        this.lastServiceFeedback = lastServiceFeedback;
        return this;
    }
    
    public VehicleServiceHistoryBuilder withReasonForComplaint(String reasonForComplaint) {
        this.reasonForComplaint = reasonForComplaint;
        return this;
    }

    public VehicleServiceHistoryBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }

    public VehicleServiceHistoryBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public VehicleServiceHistoryBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public VehicleServiceHistoryBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public VehicleServiceHistoryBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public VehicleServiceHistoryBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public VehicleServiceHistoryBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public VehicleServiceHistoryBuilder but() {
        return aVehicleServiceHistory().withKmReading(kmReading).withRemarks(remarks).withServiceAmount(serviceAmount)
                .withServiceCenter(serviceCenter).withServiceDate(serviceDate).withServiceManager(serviceManager)
                .withMdServiceType(mdServiceType).withMdTenant(mdTenant).withServiceVehicle(serviceVehicle)
                .withCreatedBy(createdBy).withCreatedDate(createdDate).withLastModifiedBy(lastModifiedBy)
                .withLastModifiedDate(lastModifiedDate).withComplaintStatus(complaintStatus)
                .withDealerName(dealerName).withDealerLocation(dealerLocation).withReasonForComplaint(reasonForComplaint)
                .withLastServiceFeedback(lastServiceFeedback);
    }

    public VehicleServiceHistory build() {
        VehicleServiceHistory vehicleServiceHistory = new VehicleServiceHistory();
        vehicleServiceHistory.setKmReading(kmReading);
        vehicleServiceHistory.setRemarks(remarks);
        vehicleServiceHistory.setServiceAmount(serviceAmount);
        vehicleServiceHistory.setServiceCenter(serviceCenter);
        vehicleServiceHistory.setServiceDate(serviceDate);
        vehicleServiceHistory.setServiceManager(serviceManager);
        vehicleServiceHistory.setComplaintStatus(complaintStatus);
        vehicleServiceHistory.setDealerLocation(dealerLocation);
        vehicleServiceHistory.setDealerName(dealerName);
        vehicleServiceHistory.setLastServiceFeedback(lastServiceFeedback);
        vehicleServiceHistory.setReasonForComplaint(reasonForComplaint);
        vehicleServiceHistory.setMdServiceType(mdServiceType);
        vehicleServiceHistory.setMdTenant(mdTenant);
        vehicleServiceHistory.setServiceVehicle(serviceVehicle);
        vehicleServiceHistory.setCreatedBy(createdBy);
        vehicleServiceHistory.setCreatedDate(createdDate);
        vehicleServiceHistory.setLastModifiedBy(lastModifiedBy);
        vehicleServiceHistory.setLastModifiedDate(lastModifiedDate);
        return vehicleServiceHistory;
    }
}
