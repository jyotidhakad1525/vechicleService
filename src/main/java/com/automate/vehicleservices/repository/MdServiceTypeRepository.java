package com.automate.vehicleservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.repository.dtoprojection.IServiceTypeDTO;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface MdServiceTypeRepository extends CrudRepository<MdServiceType, Integer>, VehicleServiceTypeRepository {

    List<IServiceTypeDTO> findByMdTenantAndActive(MdTenant mdTenant, boolean isActive);

    // Service name might not be unique. Revisit. Better to maintain tenant config
    MdServiceType findFirstByMdTenant_TenantIdentifierAndActiveTrueAndServiceName(final String tenant,
                                                                                  final String serviceName);

    List<MdServiceType> findByMdTenantAndActiveTrueAndIdGreaterThan(MdTenant mdTenant, int greaterThan);

    MdServiceType findFirstByMdTenant_TenantIdentifierAndActiveTrueAndServiceSequenceEquals(String mdTenant,
                                                                                            int serviceSequence);

    @Query(value = "update md_service_type set is_active=0 where id=:id and tenant_id =:tenantId", nativeQuery = true)
    @Modifying
    int deactivate(int id, int tenantId);

    @Query(value = "update md_service_type set is_active=1 where id=:id and tenant_id =:tenantId", nativeQuery = true)
    @Modifying
    int activate(int id, int tenantId);

    List<MdServiceType> findAllByMdTenant_Id(int mdTenant);

    MdServiceType findByIdAndMdTenant_IdAndActiveTrue(int id, int mdTenant);
    
    @Query(value = "select * from md_service_type mst inner join md_tenant mt on mst.TENANT_ID=mt.ID where mt.ORG_IDENTIFIER =:orgId", nativeQuery = true)
    List<MdServiceType> getServiceTypeByOrgIdentifier(int orgId);
    
    @Query(value = "select * from md_service_type mst inner join md_tenant mt on mst.TENANT_ID=mt.ID inner join md_service_category mc on mst.CATEGORY_ID=mc.ID where mst.TENANT_ID=:tenant and mst.CATEGORY_ID=:subServiceCategoryId and mst.SERVICE_NAME=:serviceType limit 1", nativeQuery = true)
    MdServiceType getServiceTypeByTenantCategoryAndServiceType(int subServiceCategoryId, String serviceType, String tenant);
}
