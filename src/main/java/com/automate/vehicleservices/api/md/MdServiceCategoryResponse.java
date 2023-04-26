package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MdServiceCategoryResponse implements MdResponse {
    private int id;
    private String categoryName;
    private Boolean active;
    private ServiceGroup serviceGroup;
    private int tenantId;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public MdServiceCategoryResponse(MdServiceCategory mdServiceCategory) {
        if (Objects.isNull(mdServiceCategory))
            return;

        this.id = mdServiceCategory.getId();
        this.categoryName = mdServiceCategory.getCategoryName();
        this.active = mdServiceCategory.getActive();
        this.serviceGroup = mdServiceCategory.getServiceGroup();
        this.tenantId = mdServiceCategory.getMdTenant().getId();
        this.createdBy = mdServiceCategory.getCreatedBy();
        this.createdDate = mdServiceCategory.getCreatedDate();
        this.lastModifiedBy = mdServiceCategory.getLastModifiedBy();
        this.lastModifiedDate = mdServiceCategory.getLastModifiedDate();
    }
}
