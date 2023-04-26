package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdTenant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface MdTenantRepository extends CrudRepository<MdTenant, Integer> {

    MdTenant findByTenantIdentifier(final String tenantIdentifier);

    @Query("SELECT DISTINCT city FROM MdTenant where mdOrganization.orgIdentifier = :orgIdentifier and isActive = true")
    List<String> findDistinctCities(final @Param("orgIdentifier") String orgIdentifier);

    List<MdTenant> findByMdOrganization_OrgIdentifier(final @Param("orgIdentifier") String orgIdentifier);

    List<MdTenant> findByCityAndIsActiveTrueAndMdOrganization_OrgIdentifier(final @Param("city") String city,
                                                                            final @Param("orgIdentifier") String orgIdentifier);

    MdTenant findTenantIdentifierByMasterIdentifier(int tenant);
}

