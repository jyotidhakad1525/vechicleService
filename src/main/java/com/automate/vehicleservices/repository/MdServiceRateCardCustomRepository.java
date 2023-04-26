package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.api.model.AdditionalServiceRequest;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;

import java.util.List;

/**
 * Chandrashekar V
 */
public interface MdServiceRateCardCustomRepository {
    Iterable<RateCardItem> fetchRateCardByTenantAndVehicleModelAndServiceType(int tenant,
                                                                              String vehicleModel, int serviceType,
                                                                              FuelType variant);

    Iterable<RateCardItem> fetchRateCardByTenantAndVehicleModelAndServiceTypes(int tenant,
                                                                               String vehicleModel,
                                                                               List<Integer> serviceTypes,
                                                                               FuelType variant);


    Iterable<RateCardItem> fetchRateCardByTenantAndVehicle(int tenant, String vehicleModel,
                                                           FuelType variant);


    Iterable<RateCardItem> fetchRateCardByTenantAndServiceItemWithType(int tenant, String vehicleModel,
                                                                       List<AdditionalServiceRequest> additionalServiceRequests);

}
