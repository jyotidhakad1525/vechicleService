package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceAppointmentAdditionalService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface ServiceAppointmentAdditionalServiceRepository extends CrudRepository<ServiceAppointmentAdditionalService, Integer> {

}
