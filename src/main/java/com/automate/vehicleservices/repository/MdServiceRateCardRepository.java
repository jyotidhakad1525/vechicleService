package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceRateCard;
import com.automate.vehicleservices.entity.MdTenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
public interface MdServiceRateCardRepository extends CrudRepository<MdServiceRateCard, Integer>,
        MdServiceRateCardCustomRepository {

    List<MdServiceRateCard> findByMdTenantAndAndVehicleModelIn(MdTenant tenant, List<String> vehicleModel);

    List<MdServiceRateCard> findAllByMdTenant_Id(int tenantId);

    MdServiceRateCard findByIdAndMdTenant_Id(int id, int tenantId);
}
