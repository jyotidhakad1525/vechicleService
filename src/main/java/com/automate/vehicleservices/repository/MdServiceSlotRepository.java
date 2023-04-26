package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceSlot;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdServiceSlotRepository extends CrudRepository<MdServiceSlot, Integer> {

    MdServiceSlot findByIdAndMdTenant_IdAndActiveTrue(int id, int tenantId);

    @Query("update MdServiceSlot set active=false where id=:id and mdTenant.id=:tenantId")
    @Modifying
    void deleteByIdAndMdTenant_Id(@Param(value = "id") int id, @Param(value = "tenantId") int tenantId);

    List<MdServiceSlot> findAllByMdTenant_IdAndActiveTrue(int tenantId);


    @Query(value = "SELECT " +
            "  m.id AS ID  " +
            "FROM " +
            "  md_service_slots m " +
            "  LEFT OUTER JOIN md_tenant t ON m.tenant_id = t.id  " +
            "WHERE " +
            "  m.is_active = 1  " +
            "  AND m.DAY = :day  " +
            "  AND t.id = :tenant  " +
            "  AND (  " +
            "  (m.slot_time_from > :slotTimeFrom AND m.slot_time_from < :slotTimeTo ) " +
            "  OR  (m.slot_time_to > :slotTimeFrom AND m.slot_time_to < :slotTimeTo )  " +
            "  ) ", nativeQuery = true)
    List<Integer> overlappingSlots(@Param("day") DayOfWeek day, @Param("tenant") int tenant,
                                   @Param("slotTimeFrom") LocalTime slotTimeFrom,
                                   @Param("slotTimeTo") LocalTime slotTimeTo);

    @Query(value = "SELECT " +
            "  m.id AS ID  " +
            "FROM " +
            "  md_service_slots m " +
            "  LEFT OUTER JOIN md_tenant t ON m.tenant_id = t.id  " +
            "WHERE " +
            "  m.is_active = 1  " +
            "  AND m.DAY = :day  " +
            "  AND t.id = :tenant  " +
            "  AND (  " +
            "  (m.slot_time_from > :slotTimeFrom AND m.slot_time_from < :slotTimeTo ) " +
            "  OR  (m.slot_time_to > :slotTimeFrom AND m.slot_time_to < :slotTimeTo )  " +
            "  ) " +
            "  AND m.id <>:slotIdToUpdate ", nativeQuery = true)
    List<Integer> overlappingSlotsOtherThanGivenId(@Param("day") DayOfWeek day, @Param("tenant") int tenant,
                                                   @Param("slotTimeFrom") LocalTime slotTimeFrom,
                                                   @Param("slotTimeTo") LocalTime slotTimeTo,
                                                   @Param("slotIdToUpdate") int slotIdToUpdate);
}
