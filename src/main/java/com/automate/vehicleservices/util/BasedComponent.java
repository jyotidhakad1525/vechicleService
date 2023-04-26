package com.automate.vehicleservices.util;

import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.MdTenantService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BasedComponent {

    private final MdTenantService tenantService;

    public BasedComponent(MdTenantService tenantService) {
        this.tenantService = tenantService;
    }


    public MdOrganization getOrgIdBasedOnIdentifierId(String tenantIdentifier) {
        MdTenant mdTenant = tenantService.tenantByIdentifier(tenantIdentifier);
        if (Objects.isNull(mdTenant))
            throw new VehicleServicesException("tenant details not found!");
        return mdTenant.getMdOrganization();
    }
}
