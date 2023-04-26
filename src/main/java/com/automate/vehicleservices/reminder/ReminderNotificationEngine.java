package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class ReminderNotificationEngine {

    private final ReminderNotificationFactory reminderNotificationFactory;

    public ReminderNotificationEngine(ReminderNotificationFactory reminderNotificationFactory) {
        this.reminderNotificationFactory = reminderNotificationFactory;
    }

    public void sendNotification(ServiceReminderDetailsDTO reminderDetailsDTO) {

        log.info(String.format("Notifying %s", reminderDetailsDTO.toString()));

        ReminderNotification reminderNotification =
                reminderNotificationFactory.notificationType(reminderDetailsDTO.getCommunicationMode().getType());
        if (Objects.isNull(reminderNotification)) {
            log.error(String.format("No notification channel found for %s",
                    reminderDetailsDTO));
            return;
        }
        reminderNotification.notify(reminderDetailsDTO);
    }
}
