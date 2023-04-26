package com.automate.vehicleservices.event;

import com.automate.vehicleservices.framework.event.Event;
import com.automate.vehicleservices.reminder.event.ReminderNotificationEventData;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NotificationEvent extends Event {
    private ReminderNotificationEventData reminderNotificationEventData;
}
