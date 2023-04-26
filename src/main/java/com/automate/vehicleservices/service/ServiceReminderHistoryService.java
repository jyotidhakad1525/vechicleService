package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.ServiceReminderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chandrashekar V
 */
@Component
public class ServiceReminderHistoryService {

    private final ServiceReminderHistoryRepository serviceReminderHistoryRepository;

    @Autowired
    public ServiceReminderHistoryService(ServiceReminderHistoryRepository serviceReminderHistoryRepository) {
        this.serviceReminderHistoryRepository = serviceReminderHistoryRepository;
    }

}
