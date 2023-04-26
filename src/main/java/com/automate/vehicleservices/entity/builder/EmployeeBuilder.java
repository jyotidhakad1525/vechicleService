package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.EmployeeRoundRobinAllocation;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;

public final class EmployeeBuilder {
    private int id;
    private int masterIdentifier;
    private String cognitoUserName;
    private String name;
    private String email;
    private MdTenant tenant;
    private Boolean active;
    private String role;
    private EmployeeRoundRobinAllocation employeeRoundRobinAllocation;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private EmployeeBuilder() {
    }

    public static EmployeeBuilder anEmployee() {
        return new EmployeeBuilder();
    }

    public EmployeeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public EmployeeBuilder withMasterIdentifier(int masterIdentifier) {
        this.masterIdentifier = masterIdentifier;
        return this;
    }

    public EmployeeBuilder withCognitoUserName(String cognitoUserName) {
        this.cognitoUserName = cognitoUserName;
        return this;
    }

    public EmployeeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EmployeeBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public EmployeeBuilder withTenant(MdTenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public EmployeeBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public EmployeeBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public EmployeeBuilder withEmployeeRoundRobinAllocation(EmployeeRoundRobinAllocation employeeRoundRobinAllocation) {
        this.employeeRoundRobinAllocation = employeeRoundRobinAllocation;
        return this;
    }

    public EmployeeBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public EmployeeBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public EmployeeBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public EmployeeBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public Employee build() {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setMasterIdentifier(masterIdentifier);
        employee.setCognitoUserName(cognitoUserName);
        employee.setName(name);
        employee.setEmail(email);
        employee.setTenant(tenant);
        employee.setActive(active);
        employee.setRole(role);
        employee.setEmployeeRoundRobinAllocation(employeeRoundRobinAllocation);
        employee.setCreatedBy(createdBy);
        employee.setCreatedDate(createdDate);
        employee.setLastModifiedBy(lastModifiedBy);
        employee.setLastModifiedDate(lastModifiedDate);
        return employee;
    }
}
