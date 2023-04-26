package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.repository.dtoprojection.MdServiceCategoryDTO;
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
public interface MdServiceCategoryRepository extends CrudRepository<MdServiceCategory, Integer> {

    List<MdServiceCategoryDTO> findByMdTenantAndActive(MdTenant tenant, boolean isActive);

    MdServiceCategory findFirstByMdTenant_TenantIdentifierAndCategoryName(String tenant, String categoryName);

    @Query(value = "update md_service_category set is_active=0 where id=:id and tenant_id =:tenantId", nativeQuery =
            true)
    @Modifying
    int deactivate(@Param("id") int id, @Param("tenantId") int tenantId);

    List<MdServiceCategory> findAllByMdTenant_Id(int tenantId);

    MdServiceCategory findByIdAndMdTenant_Id(int id, int tenantId);
}
