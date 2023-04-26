package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceLogicConfigurationDetails;
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
public interface ServiceLogicConfRepository extends JpaRepository<ServiceLogicConfigurationDetails, Integer> {

    Optional<ServiceLogicConfigurationDetails> findByIdAndOrganizationId(Integer id, int orgId);

    Page<ServiceLogicConfigurationDetails> findAllByOrganizationId(int orgId, Pageable pageable);

    List<ServiceLogicConfigurationDetails> findAllByOrganizationId(int orgId);

    @Query(value = "SELECT * from service_logic_details sld where (:serviceType is null or sld.service_type = :serviceType) and (:#{#status?.name()} is null or sld.status = :#{#status?.name()}) and sld.org_id =:orgId"
            , countQuery = "SELECT count(*) from service_logic_details sld where (:serviceType is null or sld.service_type = :serviceType) and (:#{#status?.name()} is null or sld.status = :#{#status?.name()}) and sld.org_id =:orgId"
            , nativeQuery = true)
    Page<ServiceLogicConfigurationDetails> search(@Param("serviceType") Integer serviceType, @Param("status") ActiveInActiveStatus status, @Param("orgId") int orgId, Pageable pageable);
}
