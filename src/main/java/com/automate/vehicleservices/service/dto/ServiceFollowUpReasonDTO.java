package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceFollowUpReasonDetails;
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
public class ServiceFollowUpReasonDTO {
    private Integer id;
    private CommonIdNameDTO serviceType;
    private CommonIdNameDTO subServiceType;

    private String reason;
    private ActiveInActiveStatus status;

    public ServiceFollowUpReasonDTO(ServiceFollowUpReasonDetails details) {
        if (Objects.isNull(details)) return;

        this.id = details.getId();
        this.serviceType = new CommonIdNameDTO(details.getServiceType().getId(), details.getServiceType().getCategoryName());
        this.subServiceType = new CommonIdNameDTO(details.getSubServiceType().getId(), details.getSubServiceType().getServiceName());
        this.reason = details.getReason();
        this.status = details.getStatus();
    }
}

