package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.SchedulerJob;
import com.automate.vehicleservices.entity.builder.SchedulerJobBuilder;
import com.automate.vehicleservices.entity.enums.JobStatus;
import com.automate.vehicleservices.repository.SchedulerJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class SchedulerJobService {

    private final SchedulerJobRepository schedulerJobRepository;

    public SchedulerJobService(SchedulerJobRepository schedulerJobRepository) {
        this.schedulerJobRepository = schedulerJobRepository;
    }


    public SchedulerJob startReminderJob() {
        // Job history
        final var vehicleServiceReminderStatusInitiated =
                SchedulerJobBuilder.aSchedulerJob().buildVehicleServiceReminderStatusInitiated();
        return schedulerJobRepository.save(vehicleServiceReminderStatusInitiated.build());
    }

    public SchedulerJob startNotifierJob() {
        // Job history
        final var vehicleServiceReminderStatusInitiated =
                SchedulerJobBuilder.aSchedulerJob().buildVehicleServiceReminderStatusInitiated();
        return schedulerJobRepository.save(vehicleServiceReminderStatusInitiated.build());
    }

    public SchedulerJob startSchedulerJob() {
        // Job history
        final var buildVehicleServiceStatusInitiated =
                SchedulerJobBuilder.aSchedulerJob().buildVehicleServiceScheduleStatusInitiated();
        return schedulerJobRepository.save(buildVehicleServiceStatusInitiated.build());
    }


    /**
     * Once schedule is complete, update database with respective status.
     *
     * @param reminderJob
     * @param totalRecords
     * @param failedRecordsSize
     */
    public void updateJobHistoryStatus(SchedulerJob reminderJob, long totalRecords, int failedRecordsSize) {
        reminderJob.setEndTime(LocalDateTime.now());
        if (failedRecordsSize == 0) {
            reminderJob.setStatus(JobStatus.SUCCESS);
        } else if (totalRecords == failedRecordsSize) {
            // complete failure
            reminderJob.setStatus(JobStatus.FAILURE);
        } else {
            // partial failure job is failed.
            reminderJob.setStatus(JobStatus.PARTIAL_SUCCESS);
        }
        schedulerJobRepository.save(reminderJob);
        log.info(String.format("Reminder ended: The time is now %s, Total elements processed: %d",
                LocalDateTime.now(), totalRecords));
    }

}
