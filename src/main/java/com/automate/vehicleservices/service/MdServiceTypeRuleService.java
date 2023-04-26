package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.MdServiceTypeRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chandrashekar V
 */
@Component
public class MdServiceTypeRuleService {

    private final MdServiceTypeRuleRepository mdServiceTypeRuleRepository;

    @Autowired
    public MdServiceTypeRuleService(MdServiceTypeRuleRepository mdServiceTypeRuleRepository) {
        this.mdServiceTypeRuleRepository = mdServiceTypeRuleRepository;
    }

}
