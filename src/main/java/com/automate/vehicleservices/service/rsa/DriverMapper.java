package com.automate.vehicleservices.service.rsa;

import com.automate.vehicleservices.entity.Driver;
import com.automate.vehicleservices.entity.DriversAllotment;
import com.automate.vehicleservices.service.dto.DriverAllotmentModel;
import com.automate.vehicleservices.service.dto.DriverModel;
import com.automate.vehicleservices.framework.common.DateUtils;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    private final DateUtils dateUtils;

    public DriverMapper(DateUtils dateUtils) {
        this.dateUtils = dateUtils;
    }

    public Driver mapToDriverEntity(DriverModel model) {
        Driver entity = new Driver();
        entity.setId(model.getId());
        entity.setOrgId(model.getOrgId());
        entity.setBranchId(model.getBranchId());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setDriverName(model.getDriverName());
        entity.setDriverType(model.getDriverType());
        entity.setEmpId(model.getEmpId());
        entity.setModifiedBy(model.getModifiedBy());
        entity.setRemarks(model.getRemarks());
        entity.setStatus(model.getStatus());
        entity.setCreatedDatetime(this.dateUtils.parseDate(model.getCreatedDatetime()));
        entity.setModifiedDatetime(this.dateUtils.parseDate(model.getModifiedDatetime()));
        return entity;
    }

    public DriverModel mapToDriverModel(Driver entity) {
        DriverModel model = new DriverModel();
        model.setId(entity.getId());
        model.setOrgId(entity.getOrgId());
        model.setBranchId(entity.getBranchId());
        model.setCreatedBy(entity.getCreatedBy());
        model.setDriverName(entity.getDriverName());
        model.setDriverType(entity.getDriverType());
        model.setEmpId(entity.getEmpId());
        model.setModifiedBy(entity.getModifiedBy());
        model.setRemarks(entity.getRemarks());
        model.setStatus(entity.getStatus());
        model.setModifiedDatetime(this.dateUtils.formatDate(entity.getModifiedDatetime()));
        model.setCreatedDatetime(this.dateUtils.formatDate(entity.getCreatedDatetime()));
        return model;
    }

    public DriverAllotmentModel mapToDriverAllotmenModel(DriversAllotment entity) {
        DriverAllotmentModel model = new DriverAllotmentModel();
        model.setId(entity.getId());
        model.setCreatedBy(entity.getCreatedBy());
        model.setDriver(mapToDriverModel(entity.getDriver()));
        model.setModifiedBy(entity.getModifiedBy());
        model.setServiceAppointmentID(entity.getServiceAppointment());
        model.setOrgId(entity.getOrgId());
        model.setBranchd(entity.getBranchId());
        model.setInDatetime(this.dateUtils.formatDate(entity.getInDatetime()));
        model.setModifiedDatetime(this.dateUtils.formatDate(entity.getModifiedDatetime()));
        model.setPlannedEndDatetime(this.dateUtils.formatDate(entity.getPlannedEndDatetime()));
        model.setPlannedStartDatetime(this.dateUtils.formatDate(entity.getPlannedStartDatetime()));
        model.setCreatedDatetime(this.dateUtils.formatDate(entity.getCreatedDatetime()));
        return model;
    }

    public DriversAllotment mapToDriverAllotmentEntity(DriverAllotmentModel model) {
        DriversAllotment entity = new DriversAllotment();
        entity.setId(model.getId());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setDriver(mapToDriverEntity(model.getDriver()));
        entity.setModifiedBy(model.getModifiedBy());
        entity.setServiceAppointment(model.getServiceAppointmentID());
        entity.setOrgId(model.getOrgId());
        entity.setBranchId(model.getBranchd());
        entity.setCreatedDatetime(this.dateUtils.parseDate(model.getCreatedDatetime()));
        entity.setInDatetime(this.dateUtils.parseDate(model.getInDatetime()));
        entity.setModifiedDatetime(this.dateUtils.parseDate(model.getModifiedDatetime()));
        entity.setPlannedEndDatetime(this.dateUtils.parseDate(model.getPlannedEndDatetime()));
        entity.setPlannedStartDatetime(this.dateUtils.parseDate(model.getPlannedStartDatetime()));
        return entity;
    }

}