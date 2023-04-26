package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Chandrashekar V
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    List<Employee> findByActiveTrueAndTenant_Id(final int tenant);

    List<Employee> findByActiveTrueAndMasterIdentifierIn(final List<Integer> masterIdentifiers);

    Employee findByActiveTrueAndMasterIdentifier(int masterIdentifier);

    // TODO : Make Roles configurable
    @Query(value = " SELECT * " +
            "FROM  " +
            "  employee_details e   " +
            "WHERE  " +
            "  e.id NOT IN ( SELECT a.emp_id FROM employee_round_robin_allocation a )  " +
            "  and   " +
            "  e.role in (\"Tele Caller\", \"Customer Care Manager\") and e.is_active=1 ", nativeQuery = true)
    List<Employee> findEmployeesNotInAllocation();

    List<Employee> findByRoleIgnoreCase(String role);
    @Query("from Employee where tenant.mdOrganization.id=:orgId and role in ('Customer Care Executive')")
    List<Employee> getCreBasedOnOrgId(@Param("orgId") int orgId);
}
