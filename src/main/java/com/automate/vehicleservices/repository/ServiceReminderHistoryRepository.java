package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceReminderDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface ServiceReminderHistoryRepository extends CrudRepository<ServiceReminderDetails, Integer> {

}
