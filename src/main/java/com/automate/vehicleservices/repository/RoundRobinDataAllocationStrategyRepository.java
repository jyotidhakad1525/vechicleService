package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.RoundRobinDataAllocationStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRobinDataAllocationStrategyRepository extends JpaRepository<RoundRobinDataAllocationStrategy, Integer> {
    Optional<RoundRobinDataAllocationStrategy> findByIdAndOrganizationId(Integer id, int orgId);

    List<RoundRobinDataAllocationStrategy> findAllByOrganizationId(int orgId);

    @Modifying
    @Query(value = "update RoundRobinDataAllocationStrategy set isNumber=:isNumber where organization.id=:orgId ")
    int updateIsNumberValueBasedOnOrgId(@Param("isNumber") Boolean isNumber, @Param("orgId") int orgId);

    @Query(nativeQuery = true,
            value = "SELECT rrdas.* from round_robin_data_allocation_strategy rrdas inner join service_type_allocation_ratio star on rrdas.id = star.round_robin_allocation_id and star.service_type_id = :serviceTypeId WHERE rrdas.status = 'ACTIVE' and rrdas.org_id = :orgId")
    List<RoundRobinDataAllocationStrategy> getCREDetailsBasedOnOrgIdAndStatusAndServiceType(@Param("orgId") int orgId, @Param("serviceTypeId") Integer serviceTypeId);
}
