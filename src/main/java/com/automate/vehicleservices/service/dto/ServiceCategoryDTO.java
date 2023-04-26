package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCategoryDTO {
    private int id;
    private String categoryName;
    private boolean active;
    private ServiceGroup serviceGroup;
    private String tenant;

    public ServiceCategoryDTO(MdServiceCategory mdServiceCategory) {
        this.id = mdServiceCategory.getId();
        this.categoryName = mdServiceCategory.getCategoryName();
        this.active = Optional.ofNullable(mdServiceCategory.getActive()).orElse(Boolean.FALSE);
        this.serviceGroup = mdServiceCategory.getServiceGroup();
        this.tenant = mdServiceCategory.getMdTenant().getTenantIdentifier();
    }
}
