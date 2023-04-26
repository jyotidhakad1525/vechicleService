package com.automate.vehicleservices.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;

class EmployeeRoundRobinAllocationRepositoryTest extends BaseTest {

    @Autowired
    private EmployeeRoundRobinAllocationRepository employeeRoundRobinAllocationRepository;

    @Test
    void findFirstByEmployee_ActiveTrueAndEmployee_Tenant_TenantIdentifierOrderByLastAllocationTimestampAsc() {

        final var executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> allocate());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    Object lock = new Object();

    private void allocate() {
        synchronized (lock) {
            final var roundRobinAllocation =
                    employeeRoundRobinAllocationRepository.findFirstByEmployee_ActiveTrueAndEmployee_Tenant_IdOrderByLastAllocationTimestampAsc(5);
            System.out.println("Thread: " + Thread.currentThread().getName() + " , Emp- " + roundRobinAllocation.getEmployee().getId());
            roundRobinAllocation.setLastAllocationTimestamp(LocalDateTime.now());
            employeeRoundRobinAllocationRepository.save(roundRobinAllocation);
        }
    }
}