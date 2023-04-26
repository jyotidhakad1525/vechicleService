package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The persistent class for the Employee_Details database table.
 */
@Entity
@Table(name = "employee_round_robin_allocation")
@Getter
@Setter
public class EmployeeRoundRobinAllocation implements Serializable {
    private static final long serialVersionUID = -1924738847793193273L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @LastModifiedDate
    @CreatedDate
    @Column(name = "LAST_ALLOCATION_TIMESTAMP")
    private LocalDateTime lastAllocationTimestamp;
}