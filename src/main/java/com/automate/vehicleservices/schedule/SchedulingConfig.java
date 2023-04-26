package com.automate.vehicleservices.schedule;

import com.automate.vehicleservices.service.MdServiceSchedulingConfigService;
import com.automate.vehicleservices.service.dto.MdServiceSchedulingConfigDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SchedulingConfig /*implements ApplicationListener<ConfigUpdatedEvent>*/ {

    private final MdServiceSchedulingConfigService serviceSchedulingConfigService;

    private Map<String, MdServiceSchedulingConfigDTO> schedulingConfigMap = new ConcurrentHashMap<>();

    public SchedulingConfig(
            MdServiceSchedulingConfigService serviceSchedulingConfigService) {
        this.serviceSchedulingConfigService = serviceSchedulingConfigService;
    }

    /**
     * TODO: Use this for updating config when the same is changed.
     *
     * @return
     */
    /*  @Override
    public void onApplicationEvent(ConfigUpdatedEvent applicationReadyEvent) {
        schedulingConfigMap = populate();
    }*/
    private Map<String, MdServiceSchedulingConfigDTO> populate() {
        return serviceSchedulingConfigService
                .allActiveTenantsConfiguration();
    }

    public MdServiceSchedulingConfigDTO config(final String tenant) {
        if (schedulingConfigMap.isEmpty())
            schedulingConfigMap = populate();

        if (null != schedulingConfigMap && !schedulingConfigMap.isEmpty())
            return schedulingConfigMap.get(tenant);

        return null;
    }
}
