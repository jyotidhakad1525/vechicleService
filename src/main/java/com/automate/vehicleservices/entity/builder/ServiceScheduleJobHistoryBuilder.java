package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.SchedulerJob;
import com.automate.vehicleservices.entity.ServiceScheduleJobHistory;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.enums.VehicleJobStatus;

import java.time.LocalDateTime;

public final class ServiceScheduleJobHistoryBuilder {
    //bi-directional many-to-one association to ServiceVehicle
    private ServiceVehicle serviceVehicle;
    //bi-directional many-to-one association to ServiceVehicle
    private SchedulerJob schedulerJob;
    private VehicleJobStatus status;
    private String failureReason;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private ServiceScheduleJobHistoryBuilder() {
    }

    public static ServiceScheduleJobHistoryBuilder aServiceScheduleJobHistory() {
        return new ServiceScheduleJobHistoryBuilder();
    }

    public ServiceScheduleJobHistoryBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public ServiceScheduleJobHistoryBuilder withSchedulerJob(SchedulerJob schedulerJob) {
        this.schedulerJob = schedulerJob;
        return this;
    }

    public ServiceScheduleJobHistoryBuilder withStatus(VehicleJobStatus status) {
        this.status = status;
        return this;
    }

    public ServiceScheduleJobHistoryBuilder withFailureReason(String failureReason) {
        this.failureReason = failureReason;
        return this;
    }

    public ServiceScheduleJobHistoryBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceScheduleJobHistoryBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceScheduleJobHistoryBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceScheduleJobHistoryBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceScheduleJobHistory build() {
        ServiceScheduleJobHistory serviceScheduleJobHistory = new ServiceScheduleJobHistory();
        serviceScheduleJobHistory.setServiceVehicle(serviceVehicle);
        serviceScheduleJobHistory.setSchedulerJob(schedulerJob);
        serviceScheduleJobHistory.setStatus(status);
        serviceScheduleJobHistory.setFailureReason(failureReason);
        serviceScheduleJobHistory.setCreatedBy(createdBy);
        serviceScheduleJobHistory.setCreatedDate(createdDate);
        serviceScheduleJobHistory.setLastModifiedBy(lastModifiedBy);
        serviceScheduleJobHistory.setLastModifiedDate(lastModifiedDate);
        return serviceScheduleJobHistory;
    }
}
