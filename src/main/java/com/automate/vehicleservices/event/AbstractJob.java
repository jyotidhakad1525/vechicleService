package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.service.SchedulerJobService;
import com.automate.vehicleservices.service.dto.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/*import com.automate.vehicleservices.rollbar.RollBarClient;*/
/*import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.ConfigBuilder;*/

@Slf4j
public abstract class AbstractJob<T> {
    protected static final int BATCH_SIZE = 20;
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private SchedulerJobService schedulerJobService;
/*
    @Autowired
    private RollBarClient rollBarClient;*/

    /**
     * Job gets started here.
     */
    public void init() {

        if (isRunning())
            return;
        else
            setRunningToActive();

        try {
            log.info(String.format("Job %s started : The time is now %s", getJobName(), LocalDateTime.now()));
            boolean nextPageExists = true;

            // Job history
            final var schedulerJob = schedulerJobService.startSchedulerJob();

            List<String> failedRecords = new ArrayList<>();
            long totalElements = 0;
            int currentPage = 0;
            while (nextPageExists) {
                PageableResponse<T> pageableResponse = getPageableResponse(currentPage, BATCH_SIZE);

                if (Objects.isNull(pageableResponse)
                        || pageableResponse.getItems() == null
                        || pageableResponse.getItems().isEmpty())
                    break;

                totalElements = pageableResponse.getTotalElements();
                for (T t : pageableResponse.getItems()) {
                    boolean status = processEach(t, schedulerJob.getId());
                    if (!status)
                        failedRecords.add(getVehicleRegNumber(t));
                }
                nextPageExists = pageableResponse.isNextPageExists();
                currentPage = currentPage + 1;
            }

            schedulerJobService.updateJobHistoryStatus(schedulerJob, totalElements, failedRecords.size());
        } finally {
            setRunningToInactive();
        }
    }

    /**
     * Trigger schedule on each vehicle from the batch.
     *
     * @param t
     * @param jobId
     * @return
     */
    public boolean processEach(T t, final int jobId) {
        String reasonForFailure;
        try {
            process(t);
            return successEvent(t, jobId);
        } catch (Exception ex) {
          /*  Rollbar rollbar = Rollbar.init(ConfigBuilder.withAccessToken(rollBarClient.getAccessToken()).build());
            rollbar.error(ex, t != null ? t.toString() : "Scheduling error");*/
            log.error(String.format("Exception while process the vehicle %s for the job %s", getVehicleRegNumber(t),
                    getJobName()), ex);
            reasonForFailure = ex.getMessage();
        }

        return failureEvent(t, jobId, reasonForFailure);
    }

    /**
     * Fire job success event when processing is successful.
     *
     * @param t
     * @param jobId
     * @return
     */
    public boolean successEvent(T t, int jobId) {
        final var vehicleServiceScheduleSuccessEvent =
                ServiceScheduleJobSuccessEvent.ServiceScheduleJobSuccessEventBuilder
                        .aServiceScheduleJobSuccessEvent().withVehicleRegNumber(getVehicleRegNumber(t))
                        .withJobId(jobId).build();
        eventPublisher.publish(vehicleServiceScheduleSuccessEvent);
        return true;
    }

    /**
     * Fire job failure event when processing is failed.
     *
     * @param t
     * @param jobId
     * @param reasonForFailure
     * @return
     */
    public boolean failureEvent(T t, int jobId, String reasonForFailure) {
        final var vehicleServiceScheduleFailureEvent =
                ServiceScheduleJobFailureEvent.ServiceScheduleJobFailureEventBuilder
                        .aServiceScheduleJobFailureEvent()
                        .withVehicleRegNumber(getVehicleRegNumber(t))
                        .withJobId(jobId)
                        .withReasonForFailure(reasonForFailure)
                        .build();
        eventPublisher.publish(vehicleServiceScheduleFailureEvent);
        return false;
    }

    protected void setRunningToActive() {
        schedulerStatusTracker().set(true);
    }

    protected void setRunningToInactive() {
        schedulerStatusTracker().set(false);
    }

    protected boolean isRunning() {
        return schedulerStatusTracker().get();
    }

    public abstract void invokeJob();

    protected abstract void process(T t);

    public abstract AtomicBoolean schedulerStatusTracker();

    protected abstract String getVehicleRegNumber(T t);

    protected abstract PageableResponse<T> getPageableResponse(int currentPage, int batchSize);

    protected abstract String getJobName();

}
