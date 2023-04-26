package com.automate.vehicleservices.service.rsa;

import com.automate.vehicleservices.entity.DriversAllotment;
import com.automate.vehicleservices.framework.common.DateUtils;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.dto.DriverAllotmentModel;
import com.automate.vehicleservices.repository.DriversAllotmentRepository;
import com.automate.vehicleservices.repository.DriversRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * TODO : Logic needs to be revisited for all methods
 */
@Component
public class DriverAllotmentService {

    private static final Logger logger = LoggerFactory.getLogger(DriverAllotmentService.class);

    private final DriversRepository driverRepo;

    private final DriversAllotmentRepository driverAllotmentRepo;

    private final DriverMapper mapper;

    private final DateUtils dateUtils;

    public DriverAllotmentService(DriversRepository driverRepo, DriversAllotmentRepository driverAllotmentRepo,
                                  DateUtils dateUtils, DriverMapper driverMapper) {
        this.driverRepo = driverRepo;
        this.driverAllotmentRepo = driverAllotmentRepo;
        this.dateUtils = dateUtils;
        this.mapper = driverMapper;
    }

    public List<DriversAllotment> getAllotments(int orgId, int branchId, int driverId, int appointmentId) {
        logger.info("List Allotments by Org:: " + orgId + " branch:: " + branchId + " Driver Allotment Id:: " + driverId
                + " Service Appointment Id:: " + appointmentId);
        List<DriversAllotment> allotments = null;
        if (appointmentId != 0)
            allotments = driverAllotmentRepo.findAllByServiceAppointmentAndOrgIdAndBranchId(appointmentId, orgId,
                    branchId);
        else if (driverId != 0)
            allotments = driverAllotmentRepo.findAllByDriverIdAndOrgIdAndBranchId(driverId, orgId, branchId);
        else
            allotments = driverAllotmentRepo.findAllByOrgIdAndBranchId(orgId, branchId);

        return allotments;
    }


    public DriverAllotmentModel saveAllotment(DriverAllotmentModel allotment) {
        if (allotment.getId() == 0) {
            allotment.setCreatedDatetime(dateUtils.formatDate(new Date()));
            allotment.setModifiedDatetime(dateUtils.formatDate(new Date()));
            allotment.setDriver(mapper.mapToDriverModel(driverRepo.findById(allotment.getDriver().getId()).get()));
        } else {
            allotment.setModifiedDatetime(dateUtils.formatDate(new Date()));
            allotment.setDriver(mapper.mapToDriverModel(driverRepo.findById(allotment.getDriver().getId()).get()));
        }
        DriversAllotment entity = mapper.mapToDriverAllotmentEntity(allotment);
        DriversAllotment savedEntity = driverAllotmentRepo.save(entity);
        return mapper.mapToDriverAllotmenModel(savedEntity);
    }

    public void deleteAllotment(int id) {
        if (driverAllotmentRepo.existsById(id))
            driverAllotmentRepo.deleteById(id);

        throw new VehicleServicesException(String.format("Allotment not found. %d", id));
    }
}