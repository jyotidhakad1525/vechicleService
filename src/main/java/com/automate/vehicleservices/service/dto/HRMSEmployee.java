package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HRMSEmployee {
    private int empId;
    private String name;
    private String email;
    private BigInteger mobile;
    private String cognitoUserName;
    private int orgId;
    private int branchId;
    private int managerId;

    public HRMSEmployee(Employee employee) {
        if (employee == null)
            return;
        this.empId = employee.getId();
        this.cognitoUserName = employee.getCognitoUserName();
        this.name = employee.getName();
    }
}

