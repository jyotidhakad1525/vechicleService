package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceTypeCriteria;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class MdServiceTypeCriteriaResponse implements MdResponse {
    private int id;
    private String description;
    private int durationDaysTo;
    private int durationDaysFrom;
    private int mileageFrom;
    private int mileageTo;
    private int tenantId;
    private int serviceTypeId;
    private String serviceType;

    public MdServiceTypeCriteriaResponse(MdServiceTypeCriteria mdServiceTypeCriteria) {
        if (Objects.isNull(mdServiceTypeCriteria))
            return;

        this.id = mdServiceTypeCriteria.getId();
        this.description = mdServiceTypeCriteria.getDescription();
        this.durationDaysTo = mdServiceTypeCriteria.getDurationDaysTo();
        this.durationDaysFrom = mdServiceTypeCriteria.getDurationDaysFrom();
        this.mileageFrom = mdServiceTypeCriteria.getMileageFrom();
        this.mileageTo = mdServiceTypeCriteria.getMileageTo();
        this.tenantId = mdServiceTypeCriteria.getMdTenant().getId();
        final var mdServiceType = mdServiceTypeCriteria.getMdServiceType();
        if (Objects.nonNull(mdServiceType)) {
            this.serviceTypeId = mdServiceType.getId();
            this.serviceType = mdServiceType.getServiceName();
        }
    }
}
