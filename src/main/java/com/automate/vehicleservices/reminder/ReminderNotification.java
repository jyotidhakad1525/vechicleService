package com.automate.vehicleservices.reminder;


import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;

public interface ReminderNotification {
    void notify(ServiceReminderDetailsDTO reminderDetailsDTO);
}
