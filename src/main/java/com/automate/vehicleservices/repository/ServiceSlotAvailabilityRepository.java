package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceSlotAvailability;
import com.automate.vehicleservices.repository.dtoprojection.SlotAvailability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @author Chandrashekar V
 */
@Repository
public interface ServiceSlotAvailabilityRepository extends CrudRepository<ServiceSlotAvailability, Integer> {

    String FIND_SLOTS_BY_TENANT_AND_DATE = "SELECT " +
            "\tID slotID, " +
            "\tSLOT_TIME_FROM fromTime, " +
            "\tSLOT_TIME_TO toTime, " +
            "\tAVAILABILITY AS allocated, " +
            "\tAVAILABILITY - (select count(*) from service_appointment sa inner join  service_slot_availability ssa on sa.SLOT = ssa.ID where ssa.SERVICE_DATE = :serviceDate and sa.STATUS = 'BOOKED' " +
            " and ssa.SLOT_ID = mds.id) as available " +
            " FROM " +
            "\tmd_service_slots mds  " +
            " WHERE " +
            "\tmds.`DAY` = :day  " +
            "\tAND mds.TENANT_ID = :tenant";

    @Query(value = FIND_SLOTS_BY_TENANT_AND_DATE, nativeQuery = true)
    Iterable<SlotAvailability> fetchSlotsAvailability(@Param("tenant") final int tenantId,
                                                      @Param("serviceDate") final LocalDate serviceDate,
                                                      @Param("day") final String day);

}
