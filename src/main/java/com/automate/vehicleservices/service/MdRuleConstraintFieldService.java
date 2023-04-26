package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.MdRuleConstraintFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chandrashekar V
 */
@Component
public class MdRuleConstraintFieldService {

    private final MdRuleConstraintFieldRepository mdRuleConstraintFieldRepository;

    @Autowired
    public MdRuleConstraintFieldService(MdRuleConstraintFieldRepository mdRuleConstraintFieldRepository) {
        this.mdRuleConstraintFieldRepository = mdRuleConstraintFieldRepository;
    }

}
