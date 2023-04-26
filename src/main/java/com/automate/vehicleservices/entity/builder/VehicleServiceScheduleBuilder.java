package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleServiceSchedule;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class VehicleServiceScheduleBuilder {
    private int id;
    private String lastModifiedReason;
    private LocalDate serviceDueDatePreferred;
    private LocalDate serviceDueDatePerSchedule;
    private LocalDate serviceDueDateRecommended;
    private LocalDate lastServiceDate;
    private ServiceVehicle serviceVehicle;
    private MdServiceType mdServiceType;
    private MdTenant mdTenant;
    private ScheduleStatus status;
    private int kmBetweenLastTwoServices;
    private int differenceInDaysBetweenLastTwoServices;
    private float averageKM;
    private String serviceCategory;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private VehicleServiceScheduleBuilder() {
    }

    public static VehicleServiceScheduleBuilder aVehicleServiceSchedule() {
        return new VehicleServiceScheduleBuilder();
    }

    public VehicleServiceScheduleBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public VehicleServiceScheduleBuilder withLastModifiedReason(String lastModifiedReason) {
        this.lastModifiedReason = lastModifiedReason;
        return this;
    }

    public VehicleServiceScheduleBuilder withServiceDueDatePreferred(LocalDate serviceDueDatePreferred) {
        this.serviceDueDatePreferred = serviceDueDatePreferred;
        return this;
    }

    public VehicleServiceScheduleBuilder withServiceDueDatePerSchedule(LocalDate serviceDueDatePerSchedule) {
        this.serviceDueDatePerSchedule = serviceDueDatePerSchedule;
        return this;
    }

    public VehicleServiceScheduleBuilder withServiceDueDateRecommended(LocalDate serviceDueDateRecommended) {
        this.serviceDueDateRecommended = serviceDueDateRecommended;
        return this;
    }

    public VehicleServiceScheduleBuilder withLastServiceDate(LocalDate lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
        return this;
    }

    public VehicleServiceScheduleBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public VehicleServiceScheduleBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }

    public VehicleServiceScheduleBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public VehicleServiceScheduleBuilder withStatus(ScheduleStatus status) {
        this.status = status;
        return this;
    }

    public VehicleServiceScheduleBuilder withKmBetweenLastTwoServices(int kmBetweenLastTwoServices) {
        this.kmBetweenLastTwoServices = kmBetweenLastTwoServices;
        return this;
    }

    public VehicleServiceScheduleBuilder withDifferenceInDaysBetweenLastTwoServices(
            int differenceInDaysBetweenLastTwoServices) {
        this.differenceInDaysBetweenLastTwoServices = differenceInDaysBetweenLastTwoServices;
        return this;
    }

    public VehicleServiceScheduleBuilder withAverageKM(float averageKM) {
        BigDecimal bigDecimal = new BigDecimal(averageKM).setScale(2, RoundingMode.HALF_UP);
        this.averageKM = bigDecimal.floatValue();
        return this;
    }

    public VehicleServiceScheduleBuilder withServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
        return this;
    }

    public VehicleServiceScheduleBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public VehicleServiceScheduleBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public VehicleServiceScheduleBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public VehicleServiceScheduleBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public VehicleServiceSchedule build() {
        VehicleServiceSchedule vehicleServiceSchedule = new VehicleServiceSchedule();
        vehicleServiceSchedule.setId(id);
        vehicleServiceSchedule.setLastModifiedReason(lastModifiedReason);
        vehicleServiceSchedule.setServiceDueDatePreferred(serviceDueDatePreferred);
        vehicleServiceSchedule.setServiceDueDatePerSchedule(serviceDueDatePerSchedule);
        vehicleServiceSchedule.setServiceDueDateRecommended(serviceDueDateRecommended);
        vehicleServiceSchedule.setLastServiceDate(lastServiceDate);
        vehicleServiceSchedule.setServiceVehicle(serviceVehicle);
        vehicleServiceSchedule.setMdServiceType(mdServiceType);
        vehicleServiceSchedule.setMdTenant(mdTenant);
        vehicleServiceSchedule.setStatus(status);
        vehicleServiceSchedule.setKmBetweenLastTwoServices(kmBetweenLastTwoServices);
        vehicleServiceSchedule.setDifferenceInDaysBetweenLastTwoServices(differenceInDaysBetweenLastTwoServices);
        vehicleServiceSchedule.setAverageKM(averageKM);
        vehicleServiceSchedule.setServiceCategory(serviceCategory);
        vehicleServiceSchedule.setCreatedBy(createdBy);
        vehicleServiceSchedule.setCreatedDate(createdDate);
        vehicleServiceSchedule.setLastModifiedBy(lastModifiedBy);
        vehicleServiceSchedule.setLastModifiedDate(lastModifiedDate);
        return vehicleServiceSchedule;
    }
}
