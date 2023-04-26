package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdRuleConstraintField;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdRuleConstraintFieldRepository extends CrudRepository<MdRuleConstraintField, Integer> {

}
