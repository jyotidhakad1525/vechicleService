package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.entity.ServiceReminder;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ReminderData {
    private ServiceReminderDetailsDTO reminderDetailsDTO;
    private ServiceReminder serviceReminder;
    private LocalDate serviceDueDate;
    private String customerName;
    private String communicationAddress;
    private String regNumber;

    public ReminderData(ServiceReminderDetailsDTO reminderDetailsDTO,
                        ServiceReminder serviceReminder) {
        this.reminderDetailsDTO = reminderDetailsDTO;
        this.serviceReminder = serviceReminder;
        this.regNumber = reminderDetailsDTO.getVehicleRegNumber();
    }

    public ReminderData invoke() {
        serviceDueDate = null;
        customerName = null;
        communicationAddress = reminderDetailsDTO.getCommunicationAddress();
        if (serviceReminder != null) {
            final var vehicleServiceSchedule = serviceReminder.getVehicleServiceSchedule();
            if (Objects.nonNull(vehicleServiceSchedule)) {
                serviceDueDate = vehicleServiceSchedule.getServiceDueDateRecommended();
                final var serviceVehicle = vehicleServiceSchedule.getServiceVehicle();
                if (Objects.nonNull(serviceVehicle)) {
                    final var customer = serviceVehicle.getCustomer();
                    if (Objects.nonNull(customer)) {
                        customerName =
                                String.format(String.format("%s %s", customer.getFirstName(),
                                        customer.getLastName()));

                        if (StringUtils.isBlank(communicationAddress))
                            communicationAddress = customer.getEmail();
                    }
                }
            }
        }
        return this;
    }
}