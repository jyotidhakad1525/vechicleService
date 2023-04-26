package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private int id;
    private int masterIdentifier;
    private String name;
    private String role;
    private int empId;
    private String email;
    private String mobileNo;
    private boolean active;
    private int tenantId;
    private Long reportingTo;

    public EmployeeDTO(Employee employee) {
        if (employee == null)
            return;
        this.id = employee.getId();
        this.masterIdentifier = employee.getMasterIdentifier();
        this.name = employee.getName();
        this.role = employee.getRole();
    }
}
