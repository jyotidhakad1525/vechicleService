package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReminderNotificationFactory {

    @Autowired
    @Qualifier("SMSlReminderNotification")
    private ReminderNotification smsReminderNotification;
    @Autowired
    @Qualifier("EmailReminderNotification")
    private ReminderNotification emailReminderNotification;


    ReminderNotification notificationType(CommunicationModeEnum communicationMode) {
        switch (communicationMode) {
            case EMAIL:
                return emailReminderNotification;
            case SMS:
                return smsReminderNotification;
            case OFFLINE:
                return null; // An entry will be created in Follow up table
        }
        log.error(String.format("No notification channel found for %s", communicationMode));
        return null;
    }
}
