package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceEstimateResponse;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface ServiceEstimateResponseRepository extends CrudRepository<ServiceEstimateResponse, Integer>,
        JpaSpecificationExecutor<ServiceEstimateResponse> {

}
