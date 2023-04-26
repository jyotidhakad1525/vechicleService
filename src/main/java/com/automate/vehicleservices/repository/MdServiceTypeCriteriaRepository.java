package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceTypeCriteria;
import com.automate.vehicleservices.entity.MdTenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
@RepositoryRestResource(collectionResourceRel = "service-types", path = "service-type")
public interface MdServiceTypeCriteriaRepository extends CrudRepository<MdServiceTypeCriteria, Integer>,
        MdServiceCriteriaCustomRepository {

    List<MdServiceTypeCriteria> findByMdTenantOrderByDurationDaysFromAsc(MdTenant tenant);

    List<MdServiceTypeCriteria> findByMdTenantAndDurationDaysFromLessThanAndDurationDaysToGreaterThanEqual(
            MdTenant tenant, final int arg1, int arg2);

    boolean existsByMdTenant_IdAndDurationDaysFromBetweenOrDurationDaysToBetweenOrMileageFromBetweenOrMileageToBetween(
            final int tenantId, final int durationDaysFrom1, final int durationDaysTo1,
            final int durationDaysFrom2, final int durationDaysTo2,
            final int MileageFrom1, final int MileageTo1, final int MileageFrom2, final int MileageTo2
    );


    void deleteByIdAndMdTenant_Id(int id, int tenantId);

    boolean existsByIdAndMdTenant_Id(int id, int tenantId);

    List<MdServiceTypeCriteria> findAllByMdTenant_Id(int tenantId);

    MdServiceTypeCriteria findByIdAndMdTenant_Id(int id, int tenantId);
}
