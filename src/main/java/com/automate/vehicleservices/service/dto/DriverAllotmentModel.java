package com.automate.vehicleservices.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverAllotmentModel {

    private int id;

    private int orgId;

    private int branchd;

    private String createdBy;

    private String createdDatetime;

    private String inDatetime;

    private String modifiedBy;

    private String modifiedDatetime;

    private String outDatetime;

    private String plannedEndDatetime;

    private String plannedStartDatetime;

    private DriverModel driver;

    private int serviceAppointmentID;

}
