package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeDTO {

    private int id;

    private String serviceName;

    private int categoryId;

    private String categoryName;

    private ServiceGroup serviceGroup;

    private int sequence;

    public ServiceTypeDTO(final MdServiceType serviceType) {
        this.id = serviceType.getId();
        this.serviceName = serviceType.getServiceName();
        final var mdServiceCategory = serviceType.getMdServiceCategory();
        this.categoryId = mdServiceCategory.getId();
        this.categoryName = mdServiceCategory.getCategoryName();
        this.serviceGroup = mdServiceCategory.getServiceGroup();
        this.sequence = serviceType.getServiceSequence();
    }
}
