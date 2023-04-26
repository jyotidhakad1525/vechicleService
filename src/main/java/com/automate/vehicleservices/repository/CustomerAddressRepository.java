package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.CustomerAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CustomerAddressRepository extends CrudRepository<CustomerAddress, Integer> {

}
