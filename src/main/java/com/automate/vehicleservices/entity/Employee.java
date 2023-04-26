package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the Employee_Details database table.
 */
@Entity
@Table(name = "employee_details")
@Getter
@Setter
public class Employee extends Auditable implements Serializable {
    private static final long serialVersionUID = -1819842889894932926L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "master_identifier", length = 11)
    private int masterIdentifier;

    @Column(name = "cognito_username", nullable = false)
    private String cognitoUserName;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private MdTenant tenant;

    @Column(name = "is_active", columnDefinition = "TINYINT")
    private Boolean active;

    @Column
    private String role;

    @Column(name = "mobileno")
    private String mobileNo;

    @OneToOne(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private EmployeeRoundRobinAllocation employeeRoundRobinAllocation;
}