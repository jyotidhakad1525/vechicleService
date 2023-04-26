package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdRuleConstraint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdRuleConstraintRepository extends CrudRepository<MdRuleConstraint, Integer> {

}
