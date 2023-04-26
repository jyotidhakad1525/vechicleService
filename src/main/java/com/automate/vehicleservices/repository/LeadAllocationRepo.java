package com.automate.vehicleservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.automate.vehicleservices.entity.LeadAllocationDetails;

/**
 * Shubham R
 */
@Repository
public interface LeadAllocationRepo extends CrudRepository<LeadAllocationDetails, Integer> {
	@Query(value = "select * from lead_allocation_details where VEHICLE_ID=:serviceId", nativeQuery = true)
	List<LeadAllocationDetails> findByServiceVehicleId(int serviceId);
}
