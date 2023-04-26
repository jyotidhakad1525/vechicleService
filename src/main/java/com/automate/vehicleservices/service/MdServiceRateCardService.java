package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceRateCardRequest;
import com.automate.vehicleservices.api.md.MdServiceRateCardResponse;
import com.automate.vehicleservices.api.md.MdServiceRateCardResponseByVehicle;
import com.automate.vehicleservices.api.model.AdditionalServiceRequest;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceRateCardBuilder;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdServiceRateCardRepository;
import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;
import com.automate.vehicleservices.service.dto.ServiceRateCardDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author Chandrashekar V
 */
@Component
public class MdServiceRateCardService extends AbstractService implements MasterDataService {

    private final MdServiceRateCardRepository mdServiceRateCardRepository;
    @Autowired
    private MdServiceItemService mdServiceItemService;
    @Autowired
    private MdServiceTypeService mdServiceTypeService;


    @Autowired
    public MdServiceRateCardService(MdServiceRateCardRepository mdServiceRateCardRepository) {
        this.mdServiceRateCardRepository = mdServiceRateCardRepository;
    }

    /**
     * Fetches Rate card by given tenant, Vehicle Model and Service Type ID.
     *
     * @param tenantId
     * @param vehicleModel
     * @param serviceTypeId
     * @return
     */
    public Map<String, List<RateCardItem>> fetchRateCardByTenantAndVehicleAndServiceType(int tenantId,
                                                                                         String vehicleModel,
                                                                                         final FuelType variant,
                                                                                         int serviceTypeId) {

        Iterable<RateCardItem> rateCards = mdServiceRateCardRepository
                .fetchRateCardByTenantAndVehicleModelAndServiceType(tenantId, vehicleModel, serviceTypeId, variant);

        return StreamSupport.stream(rateCards.spliterator(), false)
                .collect(groupingBy(RateCardItem::getServiceName, Collectors.toList()));
    }

    /**
     * @param tenantId
     * @param vehicleModel
     * @param variant
     * @return
     */
    public Collection<ServiceRateCardDTO> fetchRateCardByTenantAndVehicle(final int tenantId,
                                                                          String vehicleModel,
                                                                          final FuelType variant) {

        Iterable<RateCardItem> rateCards = mdServiceRateCardRepository
                .fetchRateCardByTenantAndVehicle(tenantId, vehicleModel, variant);

        Map<String, ServiceRateCardDTO> dtoMap = new HashMap<>();
        for (RateCardItem rateCardItem : rateCards) {

            dtoMap.computeIfAbsent(rateCardItem.getCategory(),
                    v -> new ServiceRateCardDTO(rateCardItem.getCategory(), rateCardItem.getCategoryId()));

            final var serviceRateCardDTO = dtoMap.get(rateCardItem.getCategory());
            serviceRateCardDTO.getServiceTypeData().stream()
                    .filter(data -> rateCardItem.getServiceTypeId() ==
                            data.getServiceTypeId())
                    .findFirst().ifPresentOrElse(t -> t.getRateCardItems().add(rateCardItem), () ->
            {
                final var serviceTypeData = new ServiceRateCardDTO.ServiceTypeData(rateCardItem.getServiceName(),
                        rateCardItem.getServiceTypeId());
                serviceTypeData.getRateCardItems().add(rateCardItem);
                serviceRateCardDTO.getServiceTypeData().add(serviceTypeData);
            });

        }


        return dtoMap.values();
    }

    /**
     * Fetches rate card for the given list of services.
     *
     * @param tenant
     * @param vehicleModel
     * @param fuelType
     * @param serviceTypes
     * @return
     */
    public Map<String, List<RateCardItem>> fetchRateCardByTenantAndVehicleAndServiceTypes(int tenant,
                                                                                          String vehicleModel,
                                                                                          FuelType fuelType,
                                                                                          List<Integer> serviceTypes) {
        Iterable<RateCardItem> rateCards = mdServiceRateCardRepository
                .fetchRateCardByTenantAndVehicleModelAndServiceTypes(tenant, vehicleModel, serviceTypes,
                        fuelType);

        return StreamSupport.stream(rateCards.spliterator(), false)
                .collect(groupingBy(RateCardItem::getServiceName, Collectors.toList()));
    }

    /**
     * Fetch rate card for service types and service items combination.
     *
     * @param tenant
     * @param vehicleModel
     * @param additionalServiceRequests
     * @return
     */
    public Map<String, List<RateCardItem>> fetchRateCardByTenantAndServiceItemWithType(int tenant,
                                                                                       String vehicleModel,
                                                                                       List<AdditionalServiceRequest> additionalServiceRequests) {
        Iterable<RateCardItem> rateCards = mdServiceRateCardRepository
                .fetchRateCardByTenantAndServiceItemWithType(tenant, vehicleModel, additionalServiceRequests);

        return StreamSupport.stream(rateCards.spliterator(), false)
                .collect(groupingBy(RateCardItem::getServiceName, Collectors.toList()));
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdServiceRateCardRequest mdServiceRateCardRequest = (MdServiceRateCardRequest) mdRequest;
        final var mdServiceItem = mdServiceItemService.findByIdAndTenant(mdServiceRateCardRequest.getMdServiceItem(),
                tenantId);
        if (Objects.isNull(mdServiceItem))
            throw new VehicleServicesException("Invalid service item");

        final var mdServiceType = mdServiceTypeService.getMdServiceType(mdServiceRateCardRequest.getMdServiceType(),
                tenantId);
        if (Objects.isNull(mdServiceType))
            throw new VehicleServicesException("Invalid service type");

        final var mdTenantOptional = crudService.findById(tenantId, MdTenant.class);
        final var mdServiceRateCard = MdServiceRateCardBuilder.aMdServiceRateCard()
                .withMdServiceItem(mdServiceItem)
                .withMdServiceType(mdServiceType)
                .withRate(mdServiceRateCardRequest.getRate())
                .withApplicableForPetrol(mdServiceRateCardRequest.getApplicableForPetrol())
                .withApplicableForDiesel(mdServiceRateCardRequest.getApplicableForDiesel())
                .withApplicableForHybrid(mdServiceRateCardRequest.getApplicableForHybrid())
                .withApplicableForElectric(mdServiceRateCardRequest.getApplicableForElectric())
                .withVehicleModel(mdServiceRateCardRequest.getVehicleModel())
                .withMdTenant(mdTenantOptional.get())
                .build();
        final var save = crudService.save(mdServiceRateCard);
        return new MdServiceRateCardResponse(save);
    }

    @Override
    public boolean delete(int id, int tenantId) {
        return false;
    }

    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var serviceRateCardList = mdServiceRateCardRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(serviceRateCardList))
            return Collections.emptyList();
        MdServiceRateCardResponseByVehicle mdServiceRateCardResponseByVehicle =
                new MdServiceRateCardResponseByVehicle();
        serviceRateCardList.forEach(mdServiceRateCardResponseByVehicle::addRateCardEntry);
        return List.of(mdServiceRateCardResponseByVehicle);
    }

    @Override
    public MdResponse findById(int id, int tenantId) {
        final var mdServiceRateCard = mdServiceRateCardRepository.findByIdAndMdTenant_Id(id, tenantId);
        return new MdServiceRateCardResponse(mdServiceRateCard);
    }
}
