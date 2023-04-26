package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceLogicReminderDetails;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceLogicReminderDTO {
    private Integer id;
    private CommonIdNameDTO serviceType;
    private CommonIdNameDTO subServiceType;

    private Integer reminderDays;
    private ActiveInActiveStatus status;

    public ServiceLogicReminderDTO(ServiceLogicReminderDetails details) {
        if (Objects.isNull(details)) return;

        this.id = details.getId();
        this.serviceType = new CommonIdNameDTO(details.getServiceType().getId(), details.getServiceType().getCategoryName());
        this.subServiceType = new CommonIdNameDTO(details.getSubServiceType().getId(), details.getSubServiceType().getServiceName());
        this.reminderDays = details.getReminderDays();
        this.status = details.getStatus();
    }
}

