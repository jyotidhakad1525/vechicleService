package com.automate.vehicleservices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automate.vehicleservices.entity.OrgDataAllocationStrategyType;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.automate.vehicleservices.entity.enums.AllocationType;

@Repository
public interface OrgDataAllocationStrategyTypeRepository extends JpaRepository<OrgDataAllocationStrategyType, Integer> {


    List<OrgDataAllocationStrategyType> findAllByOrganizationId(int orgId);

    Optional<OrgDataAllocationStrategyType> findAllByOrganizationIdAndStatus(int orgId, ActiveInActiveStatus status);
 
}
