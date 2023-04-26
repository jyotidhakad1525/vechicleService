package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.repository.dtoprojection.ServiceItemByGroup;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public class VehicleServiceTypeRepositoryImpl implements VehicleServiceTypeRepository {

    private final EntityManager entityManager;

    public VehicleServiceTypeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * For the given tenant and service group, this query fetches service type and respective service items.
     *
     * @param tenant
     * @param serviceGroup
     * @return
     */
    @Override
    public List<ServiceItemByGroup> fetchServiceItemsByCategoryGroup(final int tenant,
                                                                     final ServiceGroup serviceGroup) {

        String query = "SELECT " +
                " i.`NAME` itemName, " +
                " x.SERVICE_ITEM_ID serviceItemId, " +
                " x.SERVICE_TYPE_ID  serviceTypeId " +
                "FROM " +
                " md_service_type_x_service_item x " +
                " JOIN md_service_item i ON x.SERVICE_ITEM_ID = i.ID " +
                " JOIN md_service_type s ON s.ID = x.SERVICE_TYPE_ID  " +
                " AND s.CATEGORY_ID IN ( SELECT c.ID FROM md_service_category c WHERE c.SERVICE_GROUP = " +
                " :serviceGroup ) " +
                " and i.TENANT_ID = :tenant";

        Query serviceItemsByServiceGroup = entityManager.createNativeQuery(query, "serviceItemsByServiceGroup");
        serviceItemsByServiceGroup.setParameter("tenant", tenant);
        serviceItemsByServiceGroup.setParameter("serviceGroup", serviceGroup.name());

        List<ServiceItemByGroup> resultList = serviceItemsByServiceGroup.getResultList();

        return resultList;

    }
}
