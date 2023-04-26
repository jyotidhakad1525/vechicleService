package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.EmployeeRoundRobinAllocation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Chandrashekar V
 */
@Repository
public interface EmployeeRoundRobinAllocationRepository extends CrudRepository<EmployeeRoundRobinAllocation, Integer> {

    EmployeeRoundRobinAllocation findFirstByEmployee_ActiveTrueAndEmployee_Tenant_IdOrderByLastAllocationTimestampAsc(
            final int tenant);


    @Query(value = "update employee_round_robin_allocation e set e.LAST_ALLOCATION_TIMESTAMP=now() where e" +
            ".emp_id=:employeeId", nativeQuery = true)
    @Modifying
    void allocate(@Param("employeeId") final int employeeId);
}
