package com.automate.vehicleservices.reminder.event;

import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReminderReminderNotificationFailureEventData extends ReminderNotificationEventData {
    private String message;

    public static final class FailureNotificationEventDataBuilder {
        private String message;
        private ServiceReminderDetailsDTO serviceReminderDetailsDTO;
        private String customer;
        private String vehicleRegNumber;
        private int serviceReminderId;
        private LocalDate serviceDate;

        private FailureNotificationEventDataBuilder() {
        }

        public static FailureNotificationEventDataBuilder aFailureNotificationEventData() {
            return new FailureNotificationEventDataBuilder();
        }

        public FailureNotificationEventDataBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public FailureNotificationEventDataBuilder withServiceReminderDetailsDTO(
                ServiceReminderDetailsDTO serviceReminderDetailsDTO) {
            this.serviceReminderDetailsDTO = serviceReminderDetailsDTO;
            return this;
        }

        public FailureNotificationEventDataBuilder withCustomer(String customer) {
            this.customer = customer;
            return this;
        }

        public FailureNotificationEventDataBuilder withVehicleRegNumber(String vehicleRegNumber) {
            this.vehicleRegNumber = vehicleRegNumber;
            return this;
        }

        public FailureNotificationEventDataBuilder withServiceReminderId(int serviceReminderId) {
            this.serviceReminderId = serviceReminderId;
            return this;
        }

        public FailureNotificationEventDataBuilder withServiceDate(LocalDate serviceDate) {
            this.serviceDate = serviceDate;
            return this;
        }

        public ReminderReminderNotificationFailureEventData build() {
            ReminderReminderNotificationFailureEventData reminderNotificationFailureEventData =
                    new ReminderReminderNotificationFailureEventData();
            reminderNotificationFailureEventData.setMessage(message);
            reminderNotificationFailureEventData.setServiceReminderDetailsDTO(serviceReminderDetailsDTO);
            reminderNotificationFailureEventData.setCustomer(customer);
            reminderNotificationFailureEventData.setVehicleRegNumber(vehicleRegNumber);
            reminderNotificationFailureEventData.setServiceReminderId(serviceReminderId);
            reminderNotificationFailureEventData.setServiceDate(serviceDate);
            return reminderNotificationFailureEventData;
        }
    }
}
