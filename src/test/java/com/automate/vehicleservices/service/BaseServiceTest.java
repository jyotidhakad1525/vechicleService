package com.automate.vehicleservices.service;

import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Chandrashekar V
 */
public abstract class BaseServiceTest extends BaseTest {

    @Autowired
    protected CrudService crudService;
}
