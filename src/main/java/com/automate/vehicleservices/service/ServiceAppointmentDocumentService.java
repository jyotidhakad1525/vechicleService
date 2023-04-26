package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.ServiceDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chandrashekar V
 */
@Component
public class ServiceAppointmentDocumentService {

    private final ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    public ServiceAppointmentDocumentService(
            ServiceDocumentRepository serviceDocumentRepository) {
        this.serviceDocumentRepository = serviceDocumentRepository;
    }

}
