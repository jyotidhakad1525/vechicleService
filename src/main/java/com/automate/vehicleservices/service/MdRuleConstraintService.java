package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.MdRuleConstraintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chandrashekar V
 */
@Component
public class MdRuleConstraintService {

    private final MdRuleConstraintRepository mdRuleConstraintRepository;

    @Autowired
    public MdRuleConstraintService(MdRuleConstraintRepository mdRuleConstraintRepository) {
        this.mdRuleConstraintRepository = mdRuleConstraintRepository;
    }

}
