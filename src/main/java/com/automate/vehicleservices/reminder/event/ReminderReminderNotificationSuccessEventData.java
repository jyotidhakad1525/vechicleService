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
public class ReminderReminderNotificationSuccessEventData extends ReminderNotificationEventData {

    private String messageBody;

    public static final class SuccessNotificationEventDataBuilder {
        private String messageBody;
        private ServiceReminderDetailsDTO serviceReminderDetailsDTO;
        private String customer;
        private String vehicleRegNumber;
        private int serviceReminderId;
        private LocalDate serviceDate;

        private SuccessNotificationEventDataBuilder() {
        }

        public static SuccessNotificationEventDataBuilder aSuccessNotificationEventData() {
            return new SuccessNotificationEventDataBuilder();
        }

        public SuccessNotificationEventDataBuilder withMessageBody(String messageBody) {
            this.messageBody = messageBody;
            return this;
        }

        public SuccessNotificationEventDataBuilder withServiceReminderDetailsDTO(
                ServiceReminderDetailsDTO serviceReminderDetailsDTO) {
            this.serviceReminderDetailsDTO = serviceReminderDetailsDTO;
            return this;
        }

        public SuccessNotificationEventDataBuilder withCustomer(String customer) {
            this.customer = customer;
            return this;
        }

        public SuccessNotificationEventDataBuilder withVehicleRegNumber(String vehicleRegNumber) {
            this.vehicleRegNumber = vehicleRegNumber;
            return this;
        }

        public SuccessNotificationEventDataBuilder withServiceReminderId(int serviceReminderId) {
            this.serviceReminderId = serviceReminderId;
            return this;
        }

        public SuccessNotificationEventDataBuilder withServiceDate(LocalDate serviceDate) {
            this.serviceDate = serviceDate;
            return this;
        }


        public ReminderReminderNotificationSuccessEventData build() {
            ReminderReminderNotificationSuccessEventData reminderNotificationSuccessEventData =
                    new ReminderReminderNotificationSuccessEventData();
            reminderNotificationSuccessEventData.setMessageBody(messageBody);
            reminderNotificationSuccessEventData.setServiceReminderDetailsDTO(serviceReminderDetailsDTO);
            reminderNotificationSuccessEventData.setCustomer(customer);
            reminderNotificationSuccessEventData.setVehicleRegNumber(vehicleRegNumber);
            reminderNotificationSuccessEventData.setServiceReminderId(serviceReminderId);
            reminderNotificationSuccessEventData.setServiceDate(serviceDate);
            return reminderNotificationSuccessEventData;
        }
    }
}
