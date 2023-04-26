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
public class ServiceLogicConfRequest {

    private Integer id;

    @NotNull(message = "serviceType must required!")
    private Integer serviceType;

    @NotNull(message = "subServiceType must required!")
    private Integer subServiceType;

    private Integer startDay;

    private Integer endDay;

    private Integer kmStart;

    private Integer kmEnd;

    private ActiveInActiveStatus status;

}
