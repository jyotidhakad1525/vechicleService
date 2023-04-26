package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdMaintenanceType;
import com.automate.vehicleservices.entity.MdTenant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdMaintenanceTypeRepository extends CrudRepository<MdMaintenanceType, Integer> {

    List<MdMaintenanceType> findByMdTenant(MdTenant tenant);

    MdMaintenanceType findByIdAndMdTenant_Id(int id, int tenantId);

    List<MdMaintenanceType> findAllByMdTenant_Id(int tenantId);

    @Query(value = "update md_maintenance_type set is_active=0 where id=:id and tenant_id =:tenantId", nativeQuery =
            true)
    @Modifying
    int deactivate(@Param("id") int id, @Param("tenantId") int tenantId);
}
