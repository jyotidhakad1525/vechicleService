package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.CustomerTenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerTenantRepository extends PagingAndSortingRepository<CustomerTenant, Integer>,
        JpaSpecificationExecutor<CustomerTenant> {


    Page<CustomerTenant> findByMdTenant_TenantIdentifier(final String tenant, Pageable pageable);

    List<CustomerTenant> findByMdTenant_TenantIdentifier(final String tenant);
}
