package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.EmployeeRoundRobinAllocation;

import java.time.LocalDateTime;

public final class EmployeeRoundRobinAllocationBuilder {
    private int id;
    private Employee employee;
    private LocalDateTime lastAllocationTimestamp;

    private EmployeeRoundRobinAllocationBuilder() {
    }

    public static EmployeeRoundRobinAllocationBuilder anEmployeeRoundRobinAllocation() {
        return new EmployeeRoundRobinAllocationBuilder();
    }

    public EmployeeRoundRobinAllocationBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public EmployeeRoundRobinAllocationBuilder withEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public EmployeeRoundRobinAllocationBuilder withLastAllocationTimestamp(LocalDateTime lastAllocationTimestamp) {
        this.lastAllocationTimestamp = lastAllocationTimestamp;
        return this;
    }

    public EmployeeRoundRobinAllocation build() {
        EmployeeRoundRobinAllocation employeeRoundRobinAllocation = new EmployeeRoundRobinAllocation();
        employeeRoundRobinAllocation.setId(id);
        employeeRoundRobinAllocation.setEmployee(employee);
        employeeRoundRobinAllocation.setLastAllocationTimestamp(lastAllocationTimestamp);
        return employeeRoundRobinAllocation;
    }
}
