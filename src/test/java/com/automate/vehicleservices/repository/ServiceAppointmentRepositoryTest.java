package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ServiceAppointmentRepositoryTest extends BaseTest {

    @Autowired
    ServiceAppointmentRepository serviceAppointmentRepository;

    @Test
    void updateAppointmentStatus() {
        serviceAppointmentRepository.updateAppointmentStatus( ServiceAppointmentStatus.CANCELLED, 62);

    }
}