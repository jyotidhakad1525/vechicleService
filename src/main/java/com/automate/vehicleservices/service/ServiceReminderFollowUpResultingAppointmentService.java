package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.ServiceReminderFollowUpResultingAppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceReminderFollowUpResultingAppointmentService {
    private final ServiceReminderFollowUpResultingAppointmentRepository serviceReminderFollowUpResultingAppointmentRepository;

    public ServiceReminderFollowUpResultingAppointmentService(
            ServiceReminderFollowUpResultingAppointmentRepository serviceReminderFollowUpResultingAppointmentRepository) {
        this.serviceReminderFollowUpResultingAppointmentRepository =
                serviceReminderFollowUpResultingAppointmentRepository;
    }

    @Transactional
    public void addAppointmentFollowUpMapping(int appointmentId, int followUpActivityId) {
        serviceReminderFollowUpResultingAppointmentRepository.doMapping(appointmentId, followUpActivityId);
    }
}
