package com.automate.vehicleservices.reminder.event;

import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReminderNotificationEventData {
    private ServiceReminderDetailsDTO serviceReminderDetailsDTO;
    private String customer;
    private String vehicleRegNumber;
    private int serviceReminderId;
    private LocalDate serviceDate;
}
