package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Chandrashekar V
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer>,
        JpaSpecificationExecutor<Customer>,
        CustomerSearchRepository {

    Customer findByContactNumber(final String contactNumber);

    List<Customer> findByContactNumberIn(final String... contactNumber);

    boolean existsByContactNumberIn(final String... contactNumber);

}
