package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceItem;
import com.automate.vehicleservices.entity.MdTenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface MdServiceItemRepository extends CrudRepository<MdServiceItem, Integer> {

    List<MdServiceItem> findByMdTenantAndIsActive(MdTenant mdTenant, boolean isActive);

    MdServiceItem findByIdAndMdTenant_IdAndIsActive(int id, int tenantId, boolean isActive);

    boolean existsByIdAndMdTenant_Id(int id, int tenantId);

    boolean deleteByIdAndMdTenant_Id(int id, int tenantId);

    MdServiceItem findByIdAndMdTenant_Id(int id, int tenantId);

    List<MdServiceItem> findByMdTenant_Id(final int tenantId);
}
