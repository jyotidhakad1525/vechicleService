package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author Chandrashekar V
 */
@JsonRootName("serviceType")
public interface IServiceTypeDTO {

    @JsonProperty("serviceTypeId")
    int getId();

    String getServiceName();

    @JsonProperty("categoryId")
    int getMdServiceCategory_id();

    @JsonProperty("categoryName")
    String getMdServiceCategory_categoryName();

    @JsonProperty("serviceGroup")
    ServiceGroup getMdServiceCategory_serviceGroup();
}
