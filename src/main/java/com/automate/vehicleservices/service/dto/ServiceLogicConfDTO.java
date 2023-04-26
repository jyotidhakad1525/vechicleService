package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceLogicConfigurationDetails;
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
public class ServiceLogicConfDTO {
    private Integer id;

    private CommonIdNameDTO serviceType;

    private CommonIdNameDTO subServiceType;

    private Integer startDay;

    private Integer endDay;

    private Integer kmStart;

    private Integer kmEnd;

    private ActiveInActiveStatus status;

    public ServiceLogicConfDTO(ServiceLogicConfigurationDetails details) {
        if (Objects.isNull(details))
            return;

        this.id = details.getId();
        this.serviceType = new CommonIdNameDTO(details.getServiceType().getId(), details.getServiceType().getCategoryName());
        this.subServiceType = new CommonIdNameDTO(details.getSubServiceType().getId(), details.getSubServiceType().getServiceName());
        this.startDay = details.getStartDay();
        this.endDay = details.getEndDay();
        this.kmStart = details.getKmStart();
        this.kmEnd = details.getKmEnd();
        this.status = details.getStatus();
    }
}

