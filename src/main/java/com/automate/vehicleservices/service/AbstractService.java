package com.automate.vehicleservices.service;

import com.automate.vehicleservices.framework.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AbstractService {

    @Autowired
    protected CrudService crudService;
    @Autowired
    protected MdTenantService tenantService;

    public <R, T> R process(T t) {
        validate(t);

        return null;

    }

    private <T> void validate(T t) {

    }

    protected List<? extends VehicleServiceValidator> validators() {

        return null;

    }

}
