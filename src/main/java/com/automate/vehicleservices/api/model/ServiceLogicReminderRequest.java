package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ServiceLogicReminderRequest {

    private Integer id;

    @NotNull(message = "serviceType must required!")
    private Integer serviceType;

    @NotNull(message = "subServiceType must required!")
    private Integer subServiceType;

    @NotNull(message = "reminderDays must required!")
    private Integer reminderDays;

    private ActiveInActiveStatus status;

}
