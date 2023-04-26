package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceTypeRule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdServiceTypeRuleRepository extends CrudRepository<MdServiceTypeRule, Integer> {

}
