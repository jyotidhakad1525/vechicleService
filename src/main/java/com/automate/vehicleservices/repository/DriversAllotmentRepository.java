package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.DriversAllotment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DriversAllotmentRepository
        extends JpaRepository<DriversAllotment, Integer>, JpaSpecificationExecutor<DriversAllotment> {


    @Query(value = "SELECT driver_id FROM\n"
            + "(SELECT driver_id,planned_start_datetime FROM `vehicle-services`.drivers_allotment \n" + "union all \n"
            + "SELECT driver_id,planned_end_datetime FROM `vehicle-services`.drivers_allotment)a "
            + " where left(planned_start_datetime,21) between date_sub(:date, INTERVAL 1 HOUR) "
            + "and DATE_ADD(:date, INTERVAL 1 HOUR) and driver_id in (:driverIds) ;", nativeQuery = true)
    List<Integer> findByDriverAndDate(Date date, List<Integer> driverIds);

    List<DriversAllotment> findAllByDriverIdAndOrgIdAndBranchId(int driverId, int orgId, int branchId);

    List<DriversAllotment> findAllByServiceAppointmentAndOrgIdAndBranchId(int serviceAppointmentId, int orgId,
                                                                          int branchId);

    List<DriversAllotment> findAllByOrgIdAndBranchId(int orgId, int branchId);

    @Query(value =
            "SELECT * FROM `vehicle-services`.drivers_allotment WHERE planned_start_datetime BETWEEN CAST" +
                    "(:plannedStartDatetime as DATETIME) AND CAST(:plannedEndDatetime as DATETIME);", nativeQuery =
            true)
    List<DriversAllotment> findAllByPlannedStartDatetimeBetweenPlannedEndDatetime(
            @Param("plannedStartDatetime") Date plannedStartDatetime,
            @Param("plannedEndDatetime") Date plannedEndDatetime);
}