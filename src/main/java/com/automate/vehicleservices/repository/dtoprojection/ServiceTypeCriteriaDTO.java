package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.MdServiceTypeCriteria;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTypeCriteriaDTO {
    private int durationDaysTo;
    private int durationDaysFrom;
    private int mileageFrom;
    private int mileageTo;
    private String tenant;
    private int serviceType;
    private int id;

    public ServiceTypeCriteriaDTO(MdServiceTypeCriteria serviceTypeCriteria) {
        this.durationDaysTo = serviceTypeCriteria.getDurationDaysTo();
        this.durationDaysFrom = serviceTypeCriteria.getDurationDaysFrom();
        this.mileageFrom = serviceTypeCriteria.getMileageFrom();
        this.mileageTo = serviceTypeCriteria.getMileageTo();
        this.tenant = serviceTypeCriteria.getMdTenant().getTenantIdentifier();
        this.serviceType = serviceTypeCriteria.getMdServiceType().getId();
        this.id = serviceTypeCriteria.getId();
    }
}
