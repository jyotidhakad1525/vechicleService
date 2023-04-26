package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.api.model.AdditionalServiceRequest;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Chandrashekar V
 */

@Repository
@Transactional
public class MdServiceRateCardCustomRepositoryImpl implements MdServiceRateCardCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Iterable<RateCardItem> fetchRateCardByTenantAndVehicleModelAndServiceType(int tenant, String vehicleModel,
                                                                                     int serviceType,
                                                                                     final FuelType variant) {
        String query = "SELECT " +
                " mdsi.SHORT_CODE as shortCode, " +
                "  mdsi.NAME as maintenanceItemName, " +
                "  mdsr.RATE as rate, " +
                "  mdst.SERVICE_NAME as serviceName, " +
                "  mdst.ID as serviceTypeId, " +
                "  mdmt.TYPE as maintenanceType, " +
                "  mdsc.CATEGORY_NAME category, " +
                "  mdsc.ID categoryId " +
                " FROM " +
                " md_service_rate_card mdsr " +
                " JOIN md_service_type mdst on mdst.ID=mdsr.SERVICE_TYPE_ID " +
                " JOIN md_service_category mdsc ON mdsc.ID = mdst.CATEGORY_ID " +
                " JOIN md_service_item mdsi on mdsi.ID=mdsr.SERVICE_ITEM_ID " +
                " left join md_maintenance_type mdmt on mdmt.ID=mdsr.MAINTENANCE_TYPE_ID " +
                " WHERE " +
                " mdsr.TENANT_ID = :tenant " +
                " AND mdsr.VEHICLE_MODEL = :vehicleModel " +
                " AND mdsr.SERVICE_TYPE_ID = :serviceType ";

        query = appendVehicleVariant(variant, query);
        Query nativeQuery = entityManager.createNativeQuery(query, "RateCardByTenantVehicleVariant");
        nativeQuery.setParameter("tenant", tenant);
        nativeQuery.setParameter("vehicleModel", vehicleModel);
        nativeQuery.setParameter("serviceType", serviceType);
        List<RateCardItem> resultList = nativeQuery.getResultList();

        return resultList;
    }

    @Override
    public Iterable<RateCardItem> fetchRateCardByTenantAndVehicleModelAndServiceTypes(int tenant,
                                                                                      String vehicleModel,
                                                                                      List<Integer> serviceTypes,
                                                                                      final FuelType variant) {
        String query = "SELECT " +
                " mdsi.SHORT_CODE as shortCode, " +
                "  mdsi.NAME as maintenanceItemName, " +
                "  mdsr.RATE as rate, " +
                "  mdst.SERVICE_NAME as serviceName, " +
                "  mdst.ID as serviceTypeId, " +
                "  mdmt.TYPE as maintenanceType, " +
                "  mdsc.CATEGORY_NAME category, " +
                "  mdsc.ID categoryId " +
                " FROM " +
                " md_service_rate_card mdsr " +
                " JOIN md_service_type mdst on mdst.ID=mdsr.SERVICE_TYPE_ID " +
                " JOIN md_service_category mdsc ON mdsc.ID = mdst.CATEGORY_ID " +
                " JOIN md_service_item mdsi on mdsi.ID=mdsr.SERVICE_ITEM_ID " +
                " left join md_maintenance_type mdmt on mdmt.ID=mdsr.MAINTENANCE_TYPE_ID " +
                " WHERE " +
                " mdsr.TENANT_ID = :tenant " +
                " AND mdsr.VEHICLE_MODEL = :vehicleModel " +
                " AND mdsr.SERVICE_TYPE_ID in( :serviceTypes )";

        query = appendVehicleVariant(variant, query);
        Query nativeQuery = entityManager.createNativeQuery(query, "RateCardByTenantVehicleVariant");
        nativeQuery.setParameter("tenant", tenant);
        nativeQuery.setParameter("vehicleModel", vehicleModel);
        nativeQuery.setParameter("serviceTypes", serviceTypes);
        List<RateCardItem> resultList = nativeQuery.getResultList();

        return resultList;
    }


    private String appendVehicleVariant(FuelType variant, String query) {
        if (variant == FuelType.PETROL)
            query = query.concat(" AND mdsr.APPLICABLE_FOR_PETROL = 1");
        else if (variant == FuelType.DIESEL)
            query = query.concat(" AND mdsr.APPLICABLE_FOR_DIESEL = 1");
        else if (variant == FuelType.HYBRID)
            query = query.concat(" AND mdsr.APPLICABLE_FOR_HYBRID = 1");
        else if (variant == FuelType.ELECTRIC)
            query = query.concat(" AND mdsr.APPLICABLE_FOR_ELECTRIC = 1");

        return query;
    }


    @Override
    public Iterable<RateCardItem> fetchRateCardByTenantAndVehicle(int tenant, String vehicleModel,
                                                                  final FuelType variant) {


        String query = "SELECT " +
                " mdsi.SHORT_CODE as shortCode, " +
                "  mdsi.NAME as maintenanceItemName, " +
                "  mdsr.RATE as rate, " +
                "  mdst.SERVICE_NAME as serviceName, " +
                "  mdst.ID as serviceTypeId, " +
                "  mdmt.TYPE as maintenanceType, " +
                "  mdsc.CATEGORY_NAME category, " +
                "  mdsc.ID categoryId " +
                " FROM " +
                " md_service_rate_card mdsr " +
                " JOIN md_service_type mdst on mdst.ID = mdsr.SERVICE_TYPE_ID " +
                " JOIN md_service_category mdsc ON mdsc.ID = mdst.CATEGORY_ID " +
                " JOIN md_service_item mdsi on mdsi.ID = mdsr.SERVICE_ITEM_ID " +
                " left join md_maintenance_type mdmt on mdmt.ID = mdsr.MAINTENANCE_TYPE_ID " +
                " WHERE " +
                " mdsr.TENANT_ID = :tenant " +
                " AND mdsr.VEHICLE_MODEL = :vehicleModel ORDER BY " +
                " mdst.SERVICE_NAME";

        query = appendVehicleVariant(variant, query);
        Query nativeQuery = entityManager.createNativeQuery(query, "RateCardByTenantVehicleVariant");
        nativeQuery.setParameter("tenant", tenant);
        nativeQuery.setParameter("vehicleModel", vehicleModel);


        List<RateCardItem> resultList = nativeQuery.getResultList();

        return resultList;
    }

    /**
     * Fetches rate card for service items for the given service type.
     *
     * @param tenant
     * @param vehicleModel
     * @param additionalServiceRequests
     * @return
     */
    @Override
    public Iterable<RateCardItem> fetchRateCardByTenantAndServiceItemWithType(int tenant, String vehicleModel,
                                                                              List<AdditionalServiceRequest> additionalServiceRequests) {

        String sql = "SELECT " +
                " mdsi.SHORT_CODE AS shortCode, " +
                " mdsi.NAME AS maintenanceItemName, " +
                " mdsr.RATE AS rate, " +
                " mdst.SERVICE_NAME AS serviceName, " +
                "  mdst.ID as serviceTypeId, " +
                " mdmt.TYPE AS maintenanceType, " +
                "  mdsc.CATEGORY_NAME category, " +
                "  mdsc.ID categoryId " +
                " FROM " +
                " md_service_rate_card mdsr " +
                " JOIN md_service_type mdst ON mdst.ID = mdsr.SERVICE_TYPE_ID " +
                " JOIN md_service_category mdsc ON mdsc.ID = mdst.CATEGORY_ID " +
                " JOIN md_service_item mdsi ON mdsi.ID = mdsr.SERVICE_ITEM_ID " +
                " LEFT JOIN md_maintenance_type mdmt ON mdmt.ID = mdsr.MAINTENANCE_TYPE_ID  " +
                " WHERE " +
                " mdsr.TENANT_ID = :tenant" +
                " AND mdsr.VEHICLE_MODEL = :vehicleModel" +
                " AND ( mdsr.SERVICE_TYPE_ID IN ( :serviceTypes ) AND mdsr.SERVICE_ITEM_ID IN ( :serviceItems) ) ";

        List<Integer> serviceTypes = new ArrayList<>();
        List<Integer> serviceItems = new ArrayList<>();

        for (AdditionalServiceRequest serviceRequest : additionalServiceRequests) {
            serviceTypes.add(serviceRequest.getServiceTypeId());
            serviceItems.addAll(serviceRequest.getServiceItems());
        }

        Query nativeQuery = entityManager.createNativeQuery(sql, "RateCardByTenantVehicleVariant");
        nativeQuery.setParameter("tenant", tenant);
        nativeQuery.setParameter("vehicleModel", vehicleModel);
        nativeQuery.setParameter("serviceTypes", serviceTypes);
        nativeQuery.setParameter("serviceItems", serviceItems);


        List<RateCardItem> resultList = nativeQuery.getResultList();

        return resultList;

    }
}
