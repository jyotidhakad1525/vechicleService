package com.automate.vehicleservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.automate.vehicleservices.entity.ServiceTypeBasedAllocationRatio;

@Repository
public interface ServiceTypeBasedAllocationRatioRepository extends JpaRepository<ServiceTypeBasedAllocationRatio,Integer> {
    void deleteAllByRoundRobinDataAllocationStrategyId(Integer id);
    @Query(nativeQuery = true,
            value = "select * from service_type_allocation_ratio star where star.round_robin_allocation_id =:roundRobinId")
    List<ServiceTypeBasedAllocationRatio> getServiceTypeByRoundRobinId(Integer roundRobinId);
}
