package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.TargetConfigurationDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TargetConfRepository extends JpaRepository<TargetConfigurationDetails, Integer> {

    Optional<TargetConfigurationDetails> findByIdAndOrganizationId(Integer id, int orgId);

    Page<TargetConfigurationDetails> findAllByOrganizationId(int orgId, Pageable pageable);

    Optional<TargetConfigurationDetails> findByDealerIdAndOrganizationId(String dealerCode, int orgId);

    List<TargetConfigurationDetails> findAllByOrganizationId(int orgId);
}
