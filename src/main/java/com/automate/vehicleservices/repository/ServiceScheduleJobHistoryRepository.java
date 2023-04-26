package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceScheduleJobHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface ServiceScheduleJobHistoryRepository extends CrudRepository<ServiceScheduleJobHistory, Integer> {

}
