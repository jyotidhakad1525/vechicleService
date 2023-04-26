package com.automate.vehicleservices.event.listeners;

import com.automate.vehicleservices.entity.SchedulerJob;
import com.automate.vehicleservices.entity.builder.ServiceScheduleJobHistoryBuilder;
import com.automate.vehicleservices.entity.enums.VehicleJobStatus;
import com.automate.vehicleservices.event.CancelServiceScheduleEvent;
import com.automate.vehicleservices.event.SchedulingSuccessEvent;
import com.automate.vehicleservices.event.ServiceScheduleJobFailureEvent;
import com.automate.vehicleservices.event.ServiceScheduleJobSuccessEvent;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.reminder.VehicleCoreReminderEngine;
import com.automate.vehicleservices.service.ServiceVehicleService;
import com.automate.vehicleservices.service.VehicleServiceScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * VehicleScheduleEventListener : Listens to events that impact vehicle service schedule, and trigger respective service
 * schedule workflow.
 */
@Component
@Slf4j
public class VehicleServiceScheduleEventListener {

    private final CrudService crudService;
    private final ServiceVehicleService serviceVehicleService;
    private final VehicleServiceScheduleService vehicleServiceScheduleService;
    private final VehicleCoreReminderEngine vehicleCoreReminderEngine;

    public VehicleServiceScheduleEventListener(CrudService crudService, ServiceVehicleService serviceVehicleService,
                                               VehicleServiceScheduleService vehicleServiceScheduleService,
                                               VehicleCoreReminderEngine vehicleCoreReminderEngine) {
        this.crudService = crudService;
        this.serviceVehicleService = serviceVehicleService;
        this.vehicleServiceScheduleService = vehicleServiceScheduleService;
        this.vehicleCoreReminderEngine = vehicleCoreReminderEngine;
    }

    /**
     * When schedule is success event is fired and this method listens on that event. Asynchronous event listener for
     * detaching next steps from current thread. Meaning, once vehicle scheduling is done, the next steps: persisting
     * updated scheduling status.
     *
     * @param serviceScheduleJobSuccessEvent
     */
    @EventListener(value = {ServiceScheduleJobSuccessEvent.class})
    @Async
    public void serviceScheduleSuccessAction(ServiceScheduleJobSuccessEvent serviceScheduleJobSuccessEvent) {
        log.info(String.format("Success Schedule event: ID: %s",
                serviceScheduleJobSuccessEvent.getEventId()));
        persistServiceScheduleJobHistory(serviceScheduleJobSuccessEvent);
    }

    /**
     * Persists schedule success event to job history.
     *
     * @param scheduleSuccessEvent
     */
    private void persistServiceScheduleJobHistory(ServiceScheduleJobSuccessEvent scheduleSuccessEvent) {
        final var schedulerJob = crudService.findById(scheduleSuccessEvent.getJobId(),
                SchedulerJob.class).get();

        final var regNumber = scheduleSuccessEvent.getVehicleRegNumber();
        final var byVehicleRegNumber = serviceVehicleService.findByVehicleRegNumber(regNumber);
        final var serviceScheduleJobHistory = ServiceScheduleJobHistoryBuilder
                .aServiceScheduleJobHistory()
                .withSchedulerJob(schedulerJob)
                .withServiceVehicle(byVehicleRegNumber)
                .withStatus(VehicleJobStatus.SUCCESS).build();

        crudService.save(serviceScheduleJobHistory);
    }

    /**
     * Listens to service schedule failure event. And acts on it by writing the status to schedule job history.
     *
     * @param serviceScheduleFailureEvent
     */
    @Async
    @EventListener(value = {ServiceScheduleJobFailureEvent.class})
    public void triggerScheduleWhenVehicleCreated(ServiceScheduleJobFailureEvent serviceScheduleFailureEvent) {
        log.info(String.format("Failure event: ID: %s",
                serviceScheduleFailureEvent.getEventId()));

        final var schedulerJob = crudService.findById(serviceScheduleFailureEvent.getJobId(),
                SchedulerJob.class).get();

        final var regNumber = serviceScheduleFailureEvent.getVehicleRegNumber();
        final var byVehicleRegNumber = serviceVehicleService.findByVehicleRegNumber(regNumber);
        final var serviceScheduleJobHistory = ServiceScheduleJobHistoryBuilder
                .aServiceScheduleJobHistory()
                .withSchedulerJob(schedulerJob)
                .withServiceVehicle(byVehicleRegNumber).withStatus(VehicleJobStatus.FAILED)
                .withFailureReason(serviceScheduleFailureEvent.getReasonForFailure())
                .build();

        crudService.save(serviceScheduleJobHistory);

    }

    /**
     * Listens to service schedule cancel event, And updates schedule status to cancel in the database.
     *
     * @param cancelServiceScheduleEvent
     */
    @EventListener(value = {CancelServiceScheduleEvent.class})
    public void updateVehicleScheduleStatusToCancel(CancelServiceScheduleEvent cancelServiceScheduleEvent) {
        log.info(String.format("Service schedule cancel event: ID: %s",
                cancelServiceScheduleEvent.getEventId()));

        // When schedule is cancelled, update schedule status in Database.
        final var vehcileId = cancelServiceScheduleEvent.getVehicleEventData().getServiceVehicle().getId();
        log.info(String.format("Cancelling vehicle schedule: %s", vehcileId));
        vehicleServiceScheduleService.cancelServiceScheduleForVehicle(vehcileId);
    }

    /**
     * Listens to schedule success event. And invokes reminder flow.
     *
     * @param schedulingSuccessEvent
     */
    @EventListener(value = {SchedulingSuccessEvent.class})
    public void invokeReminderFlow(SchedulingSuccessEvent schedulingSuccessEvent) {
        log.info(String.format("Service schedule success event: ID: %s",
                schedulingSuccessEvent.getEventId()));

        // Initiating reminder flow after service schedule is success.
//        vehicleCoreReminderEngine.initiateReminderFlow(schedulingSuccessEvent.getEventData());
        vehicleCoreReminderEngine.initiateReminderFlowV1(schedulingSuccessEvent.getEventData());
    }


}
