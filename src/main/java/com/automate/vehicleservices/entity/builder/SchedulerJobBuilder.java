package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.SchedulerJob;
import com.automate.vehicleservices.entity.enums.JobStatus;
import com.automate.vehicleservices.entity.enums.JobType;

import java.time.LocalDateTime;

/**
 * @author Chandrashekar V
 */
public final class SchedulerJobBuilder {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private JobType jobType;
    private JobStatus status;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private SchedulerJobBuilder() {
    }

    public static SchedulerJobBuilder aSchedulerJob() {
        return new SchedulerJobBuilder();
    }

    public SchedulerJobBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SchedulerJobBuilder withStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public SchedulerJobBuilder withEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public SchedulerJobBuilder withJobType(JobType jobType) {
        this.jobType = jobType;
        return this;
    }

    public SchedulerJobBuilder withStatus(JobStatus status) {
        this.status = status;
        return this;
    }

    public SchedulerJobBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public SchedulerJobBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public SchedulerJobBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public SchedulerJobBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public SchedulerJobBuilder but() {
        return aSchedulerJob().withId(id).withStartTime(startTime)
                .withEndTime(endTime).withJobType(jobType).withStatus(status)
                .withCreatedBy(createdBy).withCreatedDate(createdDate)
                .withLastModifiedBy(lastModifiedBy)
                .withLastModifiedDate(lastModifiedDate);
    }

    public SchedulerJobBuilder buildVehicleServiceScheduleStatusInitiated() {
        return aSchedulerJob().withStartTime(LocalDateTime.now()).withJobType(JobType.VEHICLE_SERVICE_SCHEDULE)
                .withStatus(JobStatus.INITIATED);

    }

    public SchedulerJobBuilder buildVehicleServiceReminderStatusInitiated() {
        return aSchedulerJob().withStartTime(LocalDateTime.now()).withJobType(JobType.VEHICLE_SERVICE_REMINDER)
                .withStatus(JobStatus.INITIATED);

    }

    public SchedulerJobBuilder buildVehicleServiceNotifierStatusInitiated() {
        return aSchedulerJob().withStartTime(LocalDateTime.now()).withJobType(JobType.SEND_REMINDER_TO_CUSTOMER)
                .withStatus(JobStatus.INITIATED);

    }

    public SchedulerJob build() {
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setId(id);
        schedulerJob.setStartTime(startTime);
        schedulerJob.setEndTime(endTime);
        schedulerJob.setJobType(jobType);
        schedulerJob.setStatus(status);
        schedulerJob.setCreatedBy(createdBy);
        schedulerJob.setCreatedDate(createdDate);
        schedulerJob.setLastModifiedBy(lastModifiedBy);
        schedulerJob.setLastModifiedDate(lastModifiedDate);
        return schedulerJob;
    }
}
