package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.CustomerQuery;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerQueryRepository extends PagingAndSortingRepository<CustomerQuery, Integer>,
        JpaSpecificationExecutor<CustomerQuery> {


}
