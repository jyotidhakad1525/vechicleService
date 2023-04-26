package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceLogicReminderDetails;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceLogicReminderRepository extends JpaRepository<ServiceLogicReminderDetails, Integer> {
    Optional<ServiceLogicReminderDetails> findByIdAndOrganizationId(Integer id, int orgId);

    Page<ServiceLogicReminderDetails> findAllByOrganizationId(int orgId, Pageable pageable);

    @Query(value = "SELECT * from service_logic_reminder slr where (:serviceType is null or slr.service_type = :serviceType) and (:subServiceType is null or slr.sub_service_type = :subServiceType) and (:reminderDays is null or slr.reminder_days = :reminderDays) and (:#{#status?.name()} is null or slr.status = :#{#status?.name()}) and slr.org_id =:orgId"
            , countQuery = "SELECT count(*) from service_logic_reminder slr where (:serviceType is null or slr.service_type = :serviceType) and (:subServiceType is null or slr.sub_service_type = :subServiceType) and (:reminderDays is null or slr.reminder_days = :reminderDays) and (:#{#status?.name()} is null or slr.status = :#{#status?.name()}) and slr.org_id =:orgId"
            , nativeQuery = true)
    Page<ServiceLogicReminderDetails> search(@Param("serviceType") Integer serviceType, @Param("subServiceType") Integer subServiceType, @Param("status") ActiveInActiveStatus status, @Param("reminderDays") Integer reminderDays, @Param("orgId") int orgId, Pageable pageable);

    List<ServiceLogicReminderDetails> findAllByOrganizationId(int orgId);

    Optional<ServiceLogicReminderDetails> findBySubServiceTypeId(Integer subServiceTypeId);
}
