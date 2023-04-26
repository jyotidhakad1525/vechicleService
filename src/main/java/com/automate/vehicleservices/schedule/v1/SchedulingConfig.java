package com.automate.vehicleservices.schedule.v1;

import com.automate.vehicleservices.entity.ServiceLogicConfigurationDetails;
import com.automate.vehicleservices.service.ServiceLogicConfService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("SchedulingConfigV1")
public class SchedulingConfig /*implements ApplicationListener<ConfigUpdatedEvent>*/ {

    private final ServiceLogicConfService serviceLogicConfService;

    public SchedulingConfig(ServiceLogicConfService serviceLogicConfService) {
        this.serviceLogicConfService = serviceLogicConfService;
    }


    public List<ServiceLogicConfigurationDetails> config(final String tenant) {
        return serviceLogicConfService.fetchAllServiceLogicConfBasedOnTenantId(tenant);
    }
}
