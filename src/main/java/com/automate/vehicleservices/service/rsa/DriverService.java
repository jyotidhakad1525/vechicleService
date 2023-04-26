package com.automate.vehicleservices.service.rsa;

import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.entity.Driver;
import com.automate.vehicleservices.entity.DriversAllotment;
import com.automate.vehicleservices.service.dto.DriverModel;
import com.automate.vehicleservices.service.dto.RsaAppointment;
import com.automate.vehicleservices.repository.DriversAllotmentRepository;
import com.automate.vehicleservices.repository.DriversRepository;
import com.automate.vehicleservices.framework.common.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private static final Logger logger = LoggerFactory.getLogger(DriverService.class);

    private final DriversRepository driversRepository;

    private final DriversAllotmentRepository driversAllotmentRepository;

    private final DriverMapper mapper;

    private final DateUtils dateUtils;

    public DriverService(DriversRepository driversRepository,
                         DriversAllotmentRepository driversAllotmentRepository, DriverMapper mapper,
                         DateUtils dateUtils) {
        this.driversRepository = driversRepository;
        this.driversAllotmentRepository = driversAllotmentRepository;
        this.mapper = mapper;
        this.dateUtils = dateUtils;
    }

    public List<DriverModel> getAllDriversList(int orgId, int branchId) {
        logger.info("Listing All drivers");
        List<Driver> drivers = driversRepository.findAllByOrgIdAndBranchId(orgId, branchId);

        if (CollectionUtils.isEmpty(drivers))
            return Collections.emptyList();

        return drivers.stream().map(mapper::mapToDriverModel).collect(Collectors.toList());

    }

    /**
     * TODO: Update the implementation logic. Lot fo hard-coded values and magic numbers.
     *
     * @param rsaAppointment
     * @return
     */
    public List<DriverModel> fetchDriver(RsaAppointment rsaAppointment) {
        logger.info("Fetch Driver for Allocation Start time:: " + rsaAppointment.getStartTimeSlot() + " End Time:: "
                + rsaAppointment.getEndTimeSlot());
        List<Driver> drivers = new ArrayList<>();
        List<DriverModel> driverModel = new ArrayList<DriverModel>();
        String type = rsaAppointment.getTypeOfService().equalsIgnoreCase("Door Step Service")
                ? (rsaAppointment.getDistance() > 60 ? "4W" : "2w")
                : "4W";

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateformat.parse(rsaAppointment.getStartTimeSlot());
            endDate = dateformat.parse(rsaAppointment.getEndTimeSlot());
            startDate = new Date(startDate.getTime() - 60 * 15 * 1000);
            endDate = new Date(endDate.getTime() + 60 * 15 * 1000);
        } catch (ParseException e) {
            logger.error("Unable to create start and end dates", e);
        }
        List<DriversAllotment> driversAllotments = driversAllotmentRepository
                .findAllByPlannedStartDatetimeBetweenPlannedEndDatetime(startDate, endDate);
        if (Objects.isNull(driversAllotments) || driversAllotments.isEmpty()) {
            drivers = driversRepository.findAllByDriverType(type);
        } else {
            List<Integer> driverIds = driversAllotments.stream().map(driver -> driver.getDriver().getId())
                    .collect(Collectors.toList());
            drivers = driversRepository.findByIdNotInAndDriverType(driverIds, type);
        }
        return drivers.stream().map(mapper::mapToDriverModel).collect(Collectors.toList());

    }

    public DriverModel saveDriver(DriverModel driver) {
        Driver entity = null;
        if (driver.getId() == 0) {
            driver.setCreatedDatetime(this.dateUtils.formatDate(new Date()));
            driver.setModifiedDatetime(this.dateUtils.formatDate(new Date()));
            entity = driversRepository.save(mapper.mapToDriverEntity(driver));

        } else {
            entity = driversRepository.findById(driver.getId()).get();
            driver.setModifiedDatetime(this.dateUtils.formatDate(new Date()));
            entity = driversRepository.save(mapper.mapToDriverEntity(driver));
        }
        return mapper.mapToDriverModel(entity);
    }

    public void deleteDriver(int id) {
        if (driversRepository.existsById(id))
            driversRepository.deleteById(id);

        throw new VehicleServicesException(String.format("Driver doesn't exists with id %d", id));
    }
}