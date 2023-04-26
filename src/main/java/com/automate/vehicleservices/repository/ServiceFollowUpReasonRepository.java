package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceFollowUpReasonDetails;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceFollowUpReasonRepository extends JpaRepository<ServiceFollowUpReasonDetails, Integer> {

    Optional<ServiceFollowUpReasonDetails> findByIdAndOrganizationId(Integer id, Integer orgId);

    Page<ServiceFollowUpReasonDetails> findAllByOrganizationId(int orgId, Pageable pageable);

    @Query(value = "SELECT * from service_follow_up_reason sfur where (:serviceType is null or sfur.service_type  = :serviceType) and (:subServiceType is null or sfur.sub_service_type = :subServiceType) and (:reason is null or sfur.reason  like %:reason%) and (:#{#status?.name()} is null or sfur.status = :#{#status?.name()}) and sfur.org_id =:orgId"
            , countQuery = "SELECT count(*) from service_follow_up_reason sfur where (:serviceType is null or sfur.service_type  = :serviceType) and (:subServiceType is null or sfur.sub_service_type = :subServiceType) and (:reason is null or sfur.reason  like %:reason%) and (:#{#status?.name()} is null or sfur.status = :#{#status?.name()}) and sfur.org_id =:orgId"
            , nativeQuery = true)
    Page<ServiceFollowUpReasonDetails> search(@Param("serviceType") Integer serviceType, @Param("subServiceType") Integer subServiceType, @Param("status") ActiveInActiveStatus status, @Param("reason") String reason, @Param("orgId") int orgId, Pageable pageable);
}
