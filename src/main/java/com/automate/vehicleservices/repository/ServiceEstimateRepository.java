package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceEstimate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Chandrashekar V
 */
@Repository
@Transactional
public interface ServiceEstimateRepository extends CrudRepository<ServiceEstimate, Integer>,
        JpaSpecificationExecutor<ServiceEstimate> {
}
