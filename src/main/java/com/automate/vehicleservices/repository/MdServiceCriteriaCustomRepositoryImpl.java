package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.framework.exception.VehicleServicesException;
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
public class MdServiceCriteriaCustomRepositoryImpl implements MdServiceCriteriaCustomRepository {

    private final EntityManager entityManager;

    public MdServiceCriteriaCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Returns service type to which the given vehicle would belong to based on the properties.
     *
     * @param vehicleAge
     * @param kmReading
     * @param tenant
     * @return
     */
    @Override
    public int serviceTypeCriteria(long vehicleAge, double kmReading, int tenant) {

        final String NATIVE_QUERY_IDENTIFY_SERVICE_TYPE_BY_KM_OR_VEHICLE_AGE = "SELECT DISTINCT " +
                "CASE " +
                " WHEN " +
                "  ( SELECT ONE.DURATION_DAYS_FROM FROM md_service_type_criteria ONE WHERE MILEAGE_FROM <= :kmReading" +
                " AND TENANT_ID = :tenant " +
                " " +
                " AND" +
                " MILEAGE_TO >= :kmReading ) > :age THEN " +
                "  (SELECT ID FROM md_service_type_criteria  WHERE TENANT_ID = :tenant AND MILEAGE_FROM <= :kmReading" +
                " " +
                "AND " +
                " MILEAGE_TO" +
                " >= " +
                " :kmReading)  " +
                " ELSE  " +
                "  (SELECT ID FROM md_service_type_criteria " +
                " WHERE TENANT_ID = :tenant AND " +
                "  DURATION_DAYS_FROM <= :age AND DURATION_DAYS_TO >= :age AND MILEAGE_TO >= :kmReading)  " +
                " END   from md_service_type_criteria " +
                " where TENANT_ID = :tenant";

        Query nativeQuery = entityManager.createNativeQuery(NATIVE_QUERY_IDENTIFY_SERVICE_TYPE_BY_KM_OR_VEHICLE_AGE);
        nativeQuery.setParameter("kmReading", kmReading);
        nativeQuery.setParameter("tenant", tenant);
        nativeQuery.setParameter("age", vehicleAge);
        List resultList = nativeQuery.getResultList();

        return validateAndFetchServiceType(resultList, kmReading, vehicleAge, tenant);

    }

    private int validateAndFetchServiceType(List resultList, final double km, final long age, final int tenant) {
        if (resultList.size() > 1)
            throw new VehicleServicesException(
                    String.format("Multiple service types found for for the given Tenant %s, vehicle with KM Reading " +
                            "%s and age %s. Data configuration issue", tenant, km, age));

        if (resultList.isEmpty() || resultList.get(0) == null)
            throw new VehicleServicesException(
                    String.format("No service type criteria found for the given Tenant %s, vehicle KM reading: %s " +
                            "and vehicle age: %s", tenant, km, age));

        return Integer.parseInt(String.valueOf(resultList.get(0)));
    }
}
