package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverModel {

    private int id;

    private Integer branchId;

    private String createdBy;

    private String createdDatetime;

    private int empId;

    private String driverName;

    private String driverType;

    private String modifiedBy;

    private String modifiedDatetime;

    private Integer orgId;

    private String remarks;

    private StatusEnum status;


}
