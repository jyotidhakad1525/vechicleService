package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.VehicleServiceSchedule;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface VehicleServiceScheduleRepository extends CrudRepository<VehicleServiceSchedule, Integer> {

    List<VehicleServiceSchedule> findByServiceVehicle_RegNumberAndStatusIn(final String vehicleRegNumber,
                                                                           ScheduleStatus... status);

    @Query(value = "update vehicle_service_schedule set STATUS= :toStatus where VEHICLE_ID= :vehicleId and " +
            "STATUS= :fromStatus", nativeQuery = true)
    @Modifying
    void changeVehicleScheduleStatus(@Param("vehicleId") final int vehicleId,
                                     @Param("toStatus") final String toStatus,
                                     @Param("fromStatus") final String fromStatus);

    @Query(value = "update vehicle_service_schedule set STATUS= :toStatus where VEHICLE_ID= :vehicleId",
            nativeQuery = true)
    @Modifying
    void changeVehicleScheduleStatus(@Param("vehicleId") final int vehicleId,
                                     @Param("toStatus") final String toStatus);


    Page<VehicleServiceSchedule> findByStatus(ScheduleStatus status, Pageable pageable);

    List<VehicleServiceSchedule> findByMdTenant_TenantIdentifierAndStatusIn(final String identifier,
                                                                            ScheduleStatus... status);

    List<VehicleServiceSchedule> findByServiceVehicle_RegNumber(final String vehicleRegNumber);

    long countAllByServiceVehicle_RegNumberAndStatusIn(String regNumber, ScheduleStatus... statuses);

    Optional<VehicleServiceSchedule> findFirstByServiceVehicle_idOrderByLastServiceDateDesc(int id);
}