package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.entity.enums.JobType;
import com.automate.vehicleservices.event.AbstractJob;
import com.automate.vehicleservices.service.ServiceReminderDetailsService;
import com.automate.vehicleservices.service.dto.PageableResponse;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ServiceReminderNotificationCronJobNew extends AbstractJob<ServiceReminderDetailsDTO> {

    private final ServiceReminderDetailsService serviceReminderDetailsService;
    private final ReminderNotificationEngine reminderNotificationEngine;
    AtomicBoolean schedulerRunning = new AtomicBoolean(false);

    public ServiceReminderNotificationCronJobNew(ServiceReminderDetailsService serviceReminderDetailsService,
                                                 ReminderNotificationEngine reminderNotificationEngine) {
        this.serviceReminderDetailsService = serviceReminderDetailsService;
        this.reminderNotificationEngine = reminderNotificationEngine;
    }

    @Override
    @Scheduled(cron = "${dms.vehicle-services.notifier-cron-expression}")
    public void invokeJob() {
        init();
    }

    @Override
    public AtomicBoolean schedulerStatusTracker() {
        return schedulerRunning;
    }

    @Override
    protected String getVehicleRegNumber(ServiceReminderDetailsDTO reminderDetailsDTO) {
        return reminderDetailsDTO.getVehicleRegNumber();
    }

    @Override
    protected PageableResponse<ServiceReminderDetailsDTO> getPageableResponse(int currentPage, int batchSize) {
        return serviceReminderDetailsService.remindersToBeSentToday(currentPage, BATCH_SIZE);
    }

    @Override
    protected String getJobName() {
        return JobType.VEHICLE_SERVICE_REMINDER.name();
    }


    @Override
    protected void process(ServiceReminderDetailsDTO reminderDetailsDTO) {
        reminderNotificationEngine.sendNotification(reminderDetailsDTO);
    }
}
