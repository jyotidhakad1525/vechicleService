package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.EmployeeRoundRobinAllocation;
import com.automate.vehicleservices.entity.builder.EmployeeRoundRobinAllocationBuilder;
import com.automate.vehicleservices.repository.EmployeeRepository;
import com.automate.vehicleservices.repository.EmployeeRoundRobinAllocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Chandrashekar V
 */
@Service
@Slf4j
public class EmployeeAllocationService {

    private final EmployeeRoundRobinAllocationRepository employeeRoundRobinAllocationRepository;

    private final EmployeeRepository employeeRepository;

    private final Object lock = new Object();

    private final Object lock1 = new Object();

    public EmployeeAllocationService(
            EmployeeRoundRobinAllocationRepository employeeRoundRobinAllocationRepository,
            EmployeeRepository employeeRepository) {
        this.employeeRoundRobinAllocationRepository = employeeRoundRobinAllocationRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public synchronized EmployeeRoundRobinAllocation fetchAssignableCRE(final int tenant) {

        synchronized (lock) {
            EmployeeRoundRobinAllocation employeeRoundRobinAllocation = null;
            employeeRoundRobinAllocation = employeeRoundRobinAllocationRepository
                    .findFirstByEmployee_ActiveTrueAndEmployee_Tenant_IdOrderByLastAllocationTimestampAsc(tenant);
            if (Objects.isNull(employeeRoundRobinAllocation)) {
                addEmployeesToRoundRobinFlow();
                employeeRoundRobinAllocation = employeeRoundRobinAllocationRepository
                        .findFirstByEmployee_ActiveTrueAndEmployee_Tenant_IdOrderByLastAllocationTimestampAsc(tenant);
            }
            if (employeeRoundRobinAllocation == null)
                return null;
            employeeRoundRobinAllocation.setLastAllocationTimestamp(LocalDateTime.now());
            return employeeRoundRobinAllocationRepository.save(employeeRoundRobinAllocation);
        }
    }

    @Scheduled(cron = "0 5 * * * ?")
    void checkForAnyNewEmployeesYetToBeAddedToAllocation() {
        addEmployeesToRoundRobinFlow();
    }

    private void addEmployeesToRoundRobinFlow() {
        synchronized (lock1) {
            try {
                final var employees = employeeRepository.findEmployeesNotInAllocation();
                if (CollectionUtils.isNotEmpty(employees)) {
                    final var allocationList = employees.stream().map(employee -> {
                        return EmployeeRoundRobinAllocationBuilder.anEmployeeRoundRobinAllocation()
                                .withEmployee(employee)
                                .withLastAllocationTimestamp(LocalDateTime.now()).build();
                    }).collect(Collectors.toList());

                    employeeRoundRobinAllocationRepository.saveAll(allocationList);
                }
            } catch (Exception ex) {
                log.error("Exception while adding new employee entries into allocation table.");
            }
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void allocate(final int empId) {
        employeeRoundRobinAllocationRepository.allocate(empId);
    }

}
