package com.automate.vehicleservices.repository.dtoprojection;

public interface MdLeadSourceHierarchyDTO {
    Integer getId();

    String getType();

    Integer getParentId();

    Integer getIsActive();

    String getParentType();
}
