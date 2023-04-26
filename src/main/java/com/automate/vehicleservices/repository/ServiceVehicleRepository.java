package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceVehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface ServiceVehicleRepository extends CrudRepository<ServiceVehicle, Integer>,
        JpaSpecificationExecutor<ServiceVehicle> {

    String UNSCHEDULED_VEHICLES_NATIVE_QUERY = "SELECT" +
            "  * " +
            "  FROM service_vehicle " +
            "   WHERE" +
            "  ID NOT in (" +
            "   SELECT" +
            "  VEHICLE_ID " +
            "   FROM" +
            "  vehicle_service_schedule where `STATUS` in (:statuses)) AND" +
            "  TENANT_ID in (select ID from" +
            "  md_tenant where IS_ACTIVE = 1)";

    String UNSCHEDULED_VEHICLES_COUNT_NATIVE_QUERY = "SELECT" +
            "  * " +
            "  FROM service_vehicle " +
            "   WHERE" +
            "  ID NOT in (" +
            "   SELECT" +
            "  VEHICLE_ID " +
            "   FROM" +
            "  vehicle_service_schedule where `STATUS` in (:statuses)) AND" +
            "  TENANT_ID in (select ID from" +
            "  md_tenant where IS_ACTIVE = 1)";

    @Deprecated
    String UPDATE_VEHICLE_STATUS = "UPDATE service_vehicle " +
            "SET `STATUS` = :status " +
            "WHERE " +
            " ID IN ( " +
            " SELECT " +
            " H2.VEHICLE_ID " +
            "FROM " +
            " vehicle_service_history H2 " +
            "WHERE " +
            "( SELECT DATE_ADD( MAX( H2.SERVICE_DATE ), INTERVAL :numberOfMonths MONTH ) ) < NOW() " +
            " GROUP BY H2.VEHICLE_ID ) " +
            " OR ((SELECT (DATE_ADD(PURCHASE_DATE, INTERVAL :numberOfMonths MONTH))) < NOW() " +
            " AND ID NOT IN (SELECT DISTINCT VEHICLE_ID FROM vehicle_service_history))";

    String UPDATE_VEHICLE_STATUS_INACTIVE_AND_LOST = "UPDATE \n" +
            "  service_vehicle SV \n" +
            "SET \n" +
            "  SV.STATUS = CASE WHEN SV.ID IN ( \n" +
            "SELECT \n" +
            "  VEHICLE_ID \n" +
            "FROM \n" +
            "  vehicle_service_history \n" +
            "WHERE \n" +
            "  id IN (\n" +
            "    SELECT \n" +
            "      max(id) \n" +
            "    FROM \n" +
            "      vehicle_service_history \n" +
            "    GROUP BY \n" +
            "      VEHICLE_ID\n" +
            "  ) \n" +
            "  and (\n" +
            "    tenant_id IS NULL \n" +
            "    OR tenant_id != SV.TENANT_ID \n" +
            "    or service_date is null \n" +
            "    or KM_READING is null\n" +
            "  )\n" +
            ") THEN 'LOST' WHEN ID IN (\n" +
            "    SELECT \n" +
            "      H2.VEHICLE_ID \n" +
            "    FROM \n" +
            "      vehicle_service_history H2 \n" +
            "    GROUP BY \n" +
            "      H2.VEHICLE_ID\n" +
            " HAVING DATE_ADD(MAX(H2.SERVICE_DATE), INTERVAL :numberOfMonths MONTH)< NOW() " +
            "  ) THEN 'INACTIVE_FOR_MORE_THAN_18_MONTHS' WHEN (\n" +
            "    SELECT \n" +
            "      (\n" +
            "        DATE_ADD(\n" +
            "          SV.PURCHASE_DATE, INTERVAL :numberOfMonths MONTH\n" +
            "        )\n" +
            "      )\n" +
            "  ) < NOW() \n" +
            "  AND SV.ID NOT IN (\n" +
            "    SELECT \n" +
            "      DISTINCT VEHICLE_ID \n" +
            "    FROM \n" +
            "      vehicle_service_history\n" +
            "  ) THEN 'INACTIVE_NO_HISTORY_FOR_18_MONTHS' ELSE 'ACTIVE' END;\n";

    ServiceVehicle findByRegNumber(final String registrationNumber);

    @Query(value = UNSCHEDULED_VEHICLES_NATIVE_QUERY, nativeQuery = true)
    List<ServiceVehicle> fetchUnscheduledVehicles(@Param("statuses") final List<String> statuses);

    @Query(value = UNSCHEDULED_VEHICLES_NATIVE_QUERY, countQuery = UNSCHEDULED_VEHICLES_COUNT_NATIVE_QUERY,
            nativeQuery = true)
    Page<ServiceVehicle> fetchUnscheduledVehiclesPaginated(@Param("statuses") final List<String> statuses,
                                                           Pageable pageable);

    /**
     * Mark the customers with given status when service last service date registered has been more than given number
     * of months.
     * <p>
     * Ex: Mark as INACTIVE after 18 months of inactivity from last service date.
     *
     * @param status
     * @param numberOfMonths
     * @return
     */
    @Modifying()
    @Query(value = UPDATE_VEHICLE_STATUS_INACTIVE_AND_LOST, nativeQuery = true)
    int scanAndTagVehiclesAsInActiveOrLostBasedOnActivity(@Param("numberOfMonths") float numberOfMonths);

    @Modifying
    @Query(value = "update service_vehicle set on_going_service = :serviceTypeId,on_going_service_due_date = :recommendedDueDate where id = :vehicleId"
            , nativeQuery = true)
    void updateOnGoingDueDateAndServiceTypeBasedOnVehicleId(@Param("vehicleId") int vehicleId, @Param("serviceTypeId") Integer mdServiceType, @Param("recommendedDueDate") LocalDate recommendedDueDate);
}
