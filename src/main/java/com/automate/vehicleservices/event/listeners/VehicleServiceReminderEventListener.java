package com.automate.vehicleservices.event.listeners;

import com.automate.vehicleservices.event.*;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.reminder.event.ReminderNotificationEventData;
import com.automate.vehicleservices.reminder.event.ReminderReminderNotificationFailureEventData;
import com.automate.vehicleservices.reminder.event.ReminderReminderNotificationSuccessEventData;
import com.automate.vehicleservices.service.ServiceReminderDetailsService;
import com.automate.vehicleservices.service.ServiceReminderFollowUpService;
import com.automate.vehicleservices.service.ServiceReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * VehicleScheduleEventListener : Listens to events that impact vehicle service schedule, and trigger respective service
 * schedule workflow.
 */
@Component
@Slf4j
public class VehicleServiceReminderEventListener {

    private final EventPublisher eventPublisher;
    private final ServiceReminderService serviceReminderService;
    private final ServiceReminderDetailsService serviceReminderDetailsService;
    @Autowired
    private ServiceReminderFollowUpService serviceReminderFollowUpService;

    public VehicleServiceReminderEventListener(ServiceReminderService serviceReminderService,
                                               EventPublisher eventPublisher,
                                               ServiceReminderDetailsService serviceReminderDetailsService) {
        this.serviceReminderService = serviceReminderService;
        this.eventPublisher = eventPublisher;
        this.serviceReminderDetailsService = serviceReminderDetailsService;
    }

    /**
     * When schedule is success event is fired and this method listens on that event.
     *
     * @param cancelServiceReminderEvent
     */
    @EventListener(value = {CancelServiceReminderEvent.class})
    public void cancelServiceReminders(CancelServiceReminderEvent cancelServiceReminderEvent) {
        log.info(String.format("Cancel Service reminder event: ID: %s",
                cancelServiceReminderEvent.getEventId()));
        serviceReminderService.cancelServiceReminder(cancelServiceReminderEvent.getReminderIds());

        // Publish cancel schedule event
        final var serviceVehicle = cancelServiceReminderEvent.getVehicleEventData().getServiceVehicle();
        final var cancelServiceScheduleEvent = CancelServiceScheduleEvent.builder()
                .eventData(new VehicleEventData(serviceVehicle))
                .build();
        eventPublisher.publish(cancelServiceScheduleEvent);

    }

    @EventListener(value = {NotificationEvent.class})
    public void updateReminderDetailEntryStatus(NotificationEvent notificationEvent) {
        final ReminderNotificationEventData reminderNotificationEventData =
                notificationEvent.getReminderNotificationEventData();
        log.info(String.format("Notification Event: %s",
                reminderNotificationEventData.toString()));

        if (reminderNotificationEventData instanceof ReminderReminderNotificationSuccessEventData)
            serviceReminderDetailsService.updateStatusToSuccess(reminderNotificationEventData.getServiceReminderDetailsDTO(),
                    ((ReminderReminderNotificationSuccessEventData) reminderNotificationEventData).getMessageBody());
        if (reminderNotificationEventData instanceof ReminderReminderNotificationFailureEventData) {
            ReminderReminderNotificationFailureEventData reminderNotificationFailureEventData =
                    (ReminderReminderNotificationFailureEventData) reminderNotificationEventData;
            serviceReminderDetailsService.updateStatusToFailure(reminderNotificationEventData.getServiceReminderDetailsDTO(),
                    reminderNotificationFailureEventData.getMessage());
        }
    }

    @EventListener(value = {ReminderEntrySuccessEvent.class})
    @Async
    public void initOfflineFollowUp(ReminderEntrySuccessEvent reminderEntrySuccessEvent) {
        final var serviceReminderDTO = reminderEntrySuccessEvent.getServiceReminderDTO();
//        serviceReminderFollowUpService.createFollowup(serviceReminderDTO);
        serviceReminderFollowUpService.createFollowupV1(serviceReminderDTO);
    }
}
