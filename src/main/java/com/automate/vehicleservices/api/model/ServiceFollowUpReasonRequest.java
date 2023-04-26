package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ServiceFollowUpReasonRequest {

    private Integer id;

    @NotNull(message = "serviceType must required!")
    private Integer serviceType;

    @NotNull(message = "subServiceType must required!")
    private Integer subServiceType;

    @NotBlank(message = "reason must required!")
    private String reason;

    private ActiveInActiveStatus status;

}
