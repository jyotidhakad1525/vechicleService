package com.automate.vehicleservices.repository;

import java.time.LocalDate;
import java.util.List;

import com.automate.vehicleservices.repository.dtoprojection.CreLocationProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.enums.FollowUpActivityStatus;
import com.automate.vehicleservices.entity.enums.FollowUpReason;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ServiceReminderFollowUpActivityRepository extends PagingAndSortingRepository<ServiceReminderFollowUpActivity,
        Integer>, JpaSpecificationExecutor<ServiceReminderFollowUpActivity> {

    List<ServiceReminderFollowUpActivity> findByFollowUpReasonAndServiceReminderFollowUp_Tenant_TenantIdentifier(
            FollowUpReason reason, final String tenantIdentifier);

    ServiceReminderFollowUpActivity findByIdAndServiceReminderFollowUp_IdAndServiceReminderFollowUp_Tenant_TenantIdentifier(
            final int activityId, final int followUpId, final String tenantIdentifier);

    List<ServiceReminderFollowUpActivity> findByCre_IdAndFollowUpReasonAndFollowUpActivityStatus(final int id,
                                                                                                 final FollowUpReason reason, final FollowUpActivityStatus status);

    List<ServiceReminderFollowUpActivity> findByCre_IdAndFollowUpActivityStatusAndEndDateGreaterThan(final int id,
                                                                                                     final FollowUpActivityStatus status, LocalDate date);

    List<ServiceReminderFollowUpActivity> findByCre_IdAndFollowUpActivityStatusAndEndDateLessThanEqual(final int id,
                                                                                                       final FollowUpActivityStatus status, LocalDate date);

    List<ServiceReminderFollowUpActivity> findByFollowUpReasonAndFollowUpActivityStatusAndCre_IdIn(
            final FollowUpReason reason, final FollowUpActivityStatus status, List<Integer> ids);


    List<ServiceReminderFollowUpActivity> findByFollowUpActivityStatusAndFollowUpReasonNotAndEndDateGreaterThanAndCre_IdIn(
            final FollowUpActivityStatus status, final FollowUpReason reason, LocalDate date,
            List<Integer> ids);

    List<ServiceReminderFollowUpActivity> findByFollowUpActivityStatusAndFollowUpReasonNotAndEndDateLessThanEqualAndCre_IdIn(
            final FollowUpActivityStatus status, final FollowUpReason reason, LocalDate date, List<Integer> ids);
    
    @Query(value = "select  * FROM service_follow_up_activity where CRE IS NULL", countQuery = "SELECT count(*) FROM service_follow_up_activity where CRE IS NULL",  nativeQuery = true)
    Page<ServiceReminderFollowUpActivity> findUnAllocatedLeads(Pageable pageable);

    @Query(value = "select  * FROM service_follow_up_activity where CRE IS NULL",  nativeQuery = true)
    List<ServiceReminderFollowUpActivity> findUnAllocatedLeads();

    @Query(value = "select  * FROM service_follow_up_activity where CRE IS NOT NULL",  nativeQuery = true)
    List<ServiceReminderFollowUpActivity> findAllocatedLeads();
    
    @Query(value = "select  * FROM service_follow_up_activity sfua inner join service_reminder_follow_up srfu on sfua.FOLLOW_UP_ID =srfu.ID inner join md_tenant mt on mt.ID =srfu.TENANT_ID inner join service_reminder_details srd on srd.ID =srfu.REMINDER_ID  inner join service_reminder sr on srd.SERVICE_REMINDER =sr.ID where  mt.ORG_IDENTIFIER =:orgId and  sr.SERVICE_TYPE =:serviceTypeId and sfua.CRE is null", nativeQuery = true)
    List<ServiceReminderFollowUpActivity> findUnAllocatedLeadsByOrgIdServiceType(int orgId,int serviceTypeId);

    @Query(nativeQuery = true,
            value = "SELECT sfua.* from service_follow_up_activity sfua inner join service_reminder_follow_up srfu on sfua.FOLLOW_UP_ID = srfu.ID and sfua.STATUS = 'CLOSED' and sfua.`RESULT` = 'SERVICE_BOOKED' and ro_id is null and srfu.TENANT_ID = :tenantId inner join service_reminder_details srd on srfu.REMINDER_ID = srd.ID inner join service_reminder sr on srd.SERVICE_REMINDER = sr.ID inner join service_vehicle sv on sr.VEHICLE_ID = sv.ID and sv.VIN like :vin inner join md_service_type mst on sr.SERVICE_TYPE = mst.ID inner join md_service_category msc on mst.CATEGORY_ID = msc.ID and msc.CATEGORY_NAME like %:serviceType% ")
    Optional<ServiceReminderFollowUpActivity> getBookedActiveRecordsBasedOnVinAndServiceType(@Param("vin") String vin,@Param("serviceType") String serviceType,@Param("tenantId") int tenantId);

    @Query(nativeQuery = true,value = "SELECT lad.CRE_SERVICE_CENTER_CODE as centerCode, sfua.CRE as creId from service_follow_up_activity sfua inner join service_reminder_follow_up srfu on srfu.ID = sfua.FOLLOW_UP_ID inner join lead_allocation_details lad on lad.VEHICLE_ID = srfu.VEHICLE_ID where sfua.CRE in :inHouseEmployeeIds and sfua.FOLLOW_UP_STATUS in :status and sfua.END_DATE BETWEEN :from and :to GROUP by sfua.CRE")
    List<CreLocationProjection> getCreAndCenterCode(@Param("inHouseEmployeeIds") List<Integer> inHouseEmployeeIds, @Param("status") List<String> status, @Param("from") LocalDate currentMonthFirstDay, @Param("to") LocalDate currentMonthLastDay);

    @Query(value = "select  * FROM service_follow_up_activity where CRE IS NOT NULL and FOLLOW_UP_DATE IS NOT NULL", countQuery = "SELECT count(*) FROM service_follow_up_activity where CRE IS NOT NULL",  nativeQuery = true)
    Page<ServiceReminderFollowUpActivity> findLeadsForCallAudit(Pageable pageable);

    @Query(value = "select  * FROM service_follow_up_activity where CRE IS NOT NULL and FOLLOW_UP_DATE IS NOT NULL", nativeQuery = true)
    List<ServiceReminderFollowUpActivity> findLeadsForCallAudit();

    @Query(value = "select *  from service_follow_up_activity sfua inner join service_reminder_follow_up srfu on sfua.FOLLOW_UP_ID=srfu.ID INNER join customer c on srfu.CUSTOMER_ID=c.ID group by c.ID order by sfua.CREATED_DATE desc", countQuery = "select count(*)  from service_follow_up_activity sfua inner join service_reminder_follow_up srfu on sfua.FOLLOW_UP_ID=srfu.ID INNER join customer c on srfu.CUSTOMER_ID=c.ID group by c.ID order by sfua.CREATED_DATE desc",  nativeQuery = true)
    Page<ServiceReminderFollowUpActivity> findMasterCall(Pageable pageable);

    @Query(value = "select *  from service_follow_up_activity sfua inner join service_reminder_follow_up srfu on sfua.FOLLOW_UP_ID=srfu.ID INNER join customer c on srfu.CUSTOMER_ID=c.ID group by c.ID order by sfua.CREATED_DATE desc",  nativeQuery = true)
    List<ServiceReminderFollowUpActivity> findMasterCallList();
}
