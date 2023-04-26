package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.ServiceAppointmentAdditionalServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chandrashekar V
 */
@Component
public class ServiceAppointmentAdditionalServiceService {

    private final ServiceAppointmentAdditionalServiceRepository serviceAppointmentAdditionalServiceRepository;

    @Autowired
    public ServiceAppointmentAdditionalServiceService(
            ServiceAppointmentAdditionalServiceRepository serviceAppointmentAdditionalServiceRepository) {
        this.serviceAppointmentAdditionalServiceRepository = serviceAppointmentAdditionalServiceRepository;
    }

}
