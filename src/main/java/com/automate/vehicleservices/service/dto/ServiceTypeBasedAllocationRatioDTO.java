package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceTypeBasedAllocationRatio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ServiceTypeBasedAllocationRatioDTO {


    private CommonIdNameDTO serviceType;
    private Integer ratio;

    public ServiceTypeBasedAllocationRatioDTO(ServiceTypeBasedAllocationRatio details) {
        if (details == null)
            return;
        this.serviceType = new CommonIdNameDTO(details.getServiceTypes().getId(), details.getServiceTypes().getServiceName());
        this.ratio = details.getRatio();
    }
}
