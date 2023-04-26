package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceReminderDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author Chandrashekar V
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceReminderDetailsDTO {
    private int id;
    private String communicationAddress;
    private LocalDate dateOfReminder;
    private CommunicationModeDTO communicationMode;
    private String messageBody;
    private int serviceReminderId;
    private String remarks;
    private String vehicleRegNumber;

    public ServiceReminderDetailsDTO(ServiceReminderDetails serviceReminderDetails) {
        this.id = serviceReminderDetails.getId();
        this.communicationAddress = serviceReminderDetails.getCommunicationAddress();
        this.dateOfReminder = serviceReminderDetails.getDateOfReminder();
        this.communicationMode = new CommunicationModeDTO(serviceReminderDetails.getCommunicationMode());
        this.messageBody = serviceReminderDetails.getMessageBody();
        this.serviceReminderId = serviceReminderDetails.getServiceReminder().getId();
        this.remarks = serviceReminderDetails.getRemarks();
        this.vehicleRegNumber = serviceReminderDetails.getServiceReminder().getServiceVehicle().getRegNumber();
    }
}
