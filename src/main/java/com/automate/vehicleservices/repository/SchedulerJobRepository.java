package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.SchedulerJob;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface SchedulerJobRepository extends CrudRepository<SchedulerJob, Integer> {

}
