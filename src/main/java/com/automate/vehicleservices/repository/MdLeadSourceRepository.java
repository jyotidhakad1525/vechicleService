package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdLeadSource;
import com.automate.vehicleservices.repository.dtoprojection.MdLeadSourceHierarchyDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MdLeadSourceRepository extends CrudRepository<MdLeadSource, Integer> {
    List<MdLeadSource> findAllByMdTenant_Id(int tenantId);

    @Query(value = "SELECT " +
            "  a.id id, " +
            "  a.type type, " +
            "  a.parent_id as parentId, " +
            "  b.type as parentType  " +
            "FROM " +
            "  md_lead_source a " +
            "LEFT JOIN md_lead_source b ON (a.parent_id = b.id) where a.tenant_id=:tenantId and a.IS_ACTIVE = 1 ORDER" +
            " BY parentType", nativeQuery = true)
    List<MdLeadSourceHierarchyDTO> fetchHierarchy(@Param("tenantId") int tenantId);


    MdLeadSource findByIdAndMdTenant_Id(int id, int tenantId);
}
