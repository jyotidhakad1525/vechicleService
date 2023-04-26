package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleKmTracker;
import com.automate.vehicleservices.repository.dtoprojection.VehicleKmTrackerDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleKmTrackerRepository extends CrudRepository<VehicleKmTracker, Integer> {

    List<VehicleKmTracker> findByServiceVehicleOrderByRecordedDateDesc(final ServiceVehicle serviceVehicle);

    List<VehicleKmTracker> findByServiceVehicle_RegNumberOrderByRecordedDateDesc(final String vehicleRegNumber);

    @Query(nativeQuery = true, value = "SELECT ID, max(KM_READING) kmReading, Date(RECORDED_DATE) recordedDate FROM " +
            "`vehicle_km_tracker` where " +
            "VEHICLE_ID= :vehicleId GROUP BY Date(RECORDED_DATE)  ORDER BY RECORDED_DATE desc")
    List<VehicleKmTrackerDTO> findMaxKMReadingPerRecordedDate(@Param("vehicleId") final int vehicleId);
}
