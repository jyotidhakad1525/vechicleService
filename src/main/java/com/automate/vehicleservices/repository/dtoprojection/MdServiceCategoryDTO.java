package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.enums.ServiceGroup;

/**
 * @author Chandrashekar V
 */
public interface MdServiceCategoryDTO {

    int getId();

    String getCategoryName();

    ServiceGroup getServiceGroup();
}
