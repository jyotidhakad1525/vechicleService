package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.AdditionalServiceRequest;
import com.automate.vehicleservices.api.model.DocContent;
import com.automate.vehicleservices.api.model.ServiceEstimateRequest;
import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.builder.ServiceDocumentBuilder;
import com.automate.vehicleservices.entity.builder.ServiceEstimateBuilder;
import com.automate.vehicleservices.entity.builder.ServiceEstimateResponseBuilder;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.ServiceEstimateRepository;
import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;
import com.automate.vehicleservices.service.dto.ServiceEstimateResponseDTO;
import com.automate.vehicleservices.service.facade.CustomerFacade;
import com.automate.vehicleservices.service.facade.VehicleFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Service
@Slf4j
public class ServiceEstimateService extends AbstractService {

    public static final double ZERO_DOUBLE = 0.0;
    private final MdServiceRateCardService rateCardService;
    private final MdServiceTypeCriteriaService serviceTypeCriteriaService;
    private final VehicleFacade vehicleFacade;
    private final CustomerFacade customerFacade;
    private final ServiceVehicleService serviceVehicleService;
    private final DocumentContentService documentContentService;
    private final MdVehicleDocumentTypeService mdVehicleDocumentTypeService;
    private final ServiceEstimateRepository serviceEstimateRepository;
    private final PhoneNumberUtil phoneNumberUtil;

    public ServiceEstimateService(MdServiceRateCardService rateCardService,
                                  MdServiceTypeCriteriaService serviceTypeCriteriaService,
                                  VehicleFacade vehicleFacade, CustomerFacade customerFacade,
                                  ServiceVehicleService serviceVehicleService,
                                  DocumentContentService documentContentService,
                                  MdVehicleDocumentTypeService mdVehicleDocumentTypeService,
                                  PhoneNumberUtil phoneNumberUtil,
                                  ServiceEstimateRepository serviceEstimateRepository) {
        this.rateCardService = rateCardService;
        this.serviceTypeCriteriaService = serviceTypeCriteriaService;
        this.vehicleFacade = vehicleFacade;
        this.customerFacade = customerFacade;
        this.serviceVehicleService = serviceVehicleService;
        this.documentContentService = documentContentService;
        this.mdVehicleDocumentTypeService = mdVehicleDocumentTypeService;
        this.phoneNumberUtil = phoneNumberUtil;
        this.serviceEstimateRepository = serviceEstimateRepository;
    }

    /**
     * @param estimateRequest
     * @param tenant
     * @return
     */
    @Transactional
    public ServiceEstimateResponseDTO estimate(ServiceEstimateRequest estimateRequest, String tenant) {

        // Check this vehicle is associated with a different customer.
        throwExceptionIfVehicleAssociatedWithDifferentCustomer(estimateRequest);

        estimateRequest.getVehicleInsuranceDetails();

        MdTenant mdTenant = tenantService.tenantByIdentifier(tenant);
        ServiceEstimate serviceEstimate = constructServiceEstimateEntityFrom(estimateRequest, mdTenant);
        crudService.save(serviceEstimate);

        Map<String, List<RateCardItem>> rateCardByService = new HashMap<>();
        regularMaintenance(estimateRequest, rateCardByService, mdTenant.getId());
        double additionalServicesCost = additionalServices(estimateRequest, rateCardByService, mdTenant.getId());

        // Step #4 - Calculate total cost of the estimate
        double total = calculateTotalEstimate(rateCardByService);

        // Process Documents
        processDocuments(estimateRequest, serviceEstimate, tenant);

        ServiceEstimateResponse serviceEstimateResponse = ServiceEstimateResponseBuilder
                .aServiceEstimateResponse()
                .withTotal(total)
                .withEstimation(rateCardByService)
                .withAdditionalServicesTotal(additionalServicesCost)
                .build();

        serviceEstimate.setServiceEstimateResponse(serviceEstimateResponse);
        final var save = crudService.save(serviceEstimate);

        return new ServiceEstimateResponseDTO(save);

    }

    /**
     * Checks whether vehicle is already associated with a different customer.
     *
     * @param estimateRequest
     */
    public void throwExceptionIfVehicleAssociatedWithDifferentCustomer(ServiceEstimateRequest estimateRequest) {
        String vehicleRegNumber = estimateRequest.getVehicleDetails().getVehicleRegNumber();
        ServiceVehicle serviceVehicleEntity = serviceVehicleService.findByVehicleRegNumber(vehicleRegNumber);

        if (Objects.nonNull(serviceVehicleEntity))
            if (!StringUtils.equalsIgnoreCase(serviceVehicleEntity.getCustomer().getContactNumber(),
                    estimateRequest.getCustomer().getContactNumber())) {

                throw new VehicleServicesException("Vehicle is associated with a different customer. Can not proceed " +
                        "with estimate");
            }
    }

    /**
     * Associate documents with document contents.
     *
     * @param estimateRequest
     * @param serviceEstimate
     * @param tenant
     */
    public void processDocuments(ServiceEstimateRequest estimateRequest, ServiceEstimate serviceEstimate,
                                 String tenant) {
        final var documents = estimateRequest.getDocuments();
        if (Objects.nonNull(documents))

            for (DocContent docContent : documents) {
                serviceEstimate.getServiceDocuments().add(constructServiceDocumentEntity(estimateRequest,
                        serviceEstimate, docContent, tenant));
            }
    }

    /**
     * Regular maintenance.
     *
     * @param estimateRequest
     * @param rateCardByService
     * @param tenant
     */
    public void regularMaintenance(ServiceEstimateRequest estimateRequest,
                                   Map<String, List<RateCardItem>> rateCardByService, int tenant) {
        // step #1 - identify service category
        if (estimateRequest.getServiceType() == ServiceGroup.REGULAR_MAINTENANCE) {
            Map<String, List<RateCardItem>> regularMaintenanceEstimate = calculateRegularMaintenanceEstimate(
                    estimateRequest, tenant);
            if (Objects.isNull(regularMaintenanceEstimate) || regularMaintenanceEstimate.isEmpty())
                throw new VehicleServicesException(String.format("Rate card not found for the given Vehicle: %s, " +
                                "Variant :%s, Tenant: %s", estimateRequest.getVehicleDetails().getVehicleModel(),
                        estimateRequest.getVehicleDetails().getFuelType().name(), tenant));
            rateCardByService.putAll(regularMaintenanceEstimate);
        }
    }

    /**
     * Additional services.
     *
     * @param estimateRequest
     * @param rateCardByService
     * @param tenant
     * @return
     */
    public double additionalServices(ServiceEstimateRequest estimateRequest,
                                     Map<String, List<RateCardItem>> rateCardByService, int tenant) {
        // Step #3 - Identify any additional services exist and attach it to step #2 response - This also works for
        // quick services category
        double additionalServicesCost = 0;
        List<AdditionalServiceRequest> additionalServices = estimateRequest.getAdditionalServices();

        if (Objects.nonNull(additionalServices) && !additionalServices.isEmpty()) {
            Map<String, List<RateCardItem>> additionalServicesRateCardMap = rateCardService
                    .fetchRateCardByTenantAndServiceItemWithType(tenant,
                            estimateRequest.getVehicleDetails().getVehicleModel(), additionalServices);

            if (Objects.isNull(additionalServicesRateCardMap) || additionalServicesRateCardMap.isEmpty())
                throw new VehicleServicesException(String.format("Rate card not found for the requested " +
                                "additional services. Given Vehicle: %s, Variant :%s, Tenant: %s",
                        estimateRequest.getVehicleDetails().getVehicleModel(),
                        estimateRequest.getVehicleDetails().getFuelType().name(), tenant));

            additionalServicesCost = calculateTotalEstimate(additionalServicesRateCardMap);

            // Add additional service rates to the same map from above
            rateCardByService.putAll(additionalServicesRateCardMap);
        }
        return additionalServicesCost;
    }

    /**
     * Service Document entity.
     *
     * @param estimateRequest
     * @param serviceEstimate
     * @param docContent
     * @param tenant
     * @return
     */
    public ServiceDocument constructServiceDocumentEntity(ServiceEstimateRequest estimateRequest,
                                                          ServiceEstimate serviceEstimate, DocContent docContent,
                                                          String tenant) {
        MdVehicleDocumentType mdDocumentType = mdVehicleDocumentTypeService
                .findByTenantDocumentNameAndLabel(tenant, docContent.getDocumentName(), docContent.getLabel());

        if (Objects.isNull(mdDocumentType))
            throw new VehicleServicesException(
                    String.format("Document name: %s, and label : %s,  combination is not accepted by the " + "tenant" +
                            " %s", docContent.getDocumentName(), docContent.getLabel(), tenant));

        DocumentContent documentContent = fetchOrCreateDocumentContentEntity(docContent);

        return ServiceDocumentBuilder.aServiceDocument()
                .withCustomer(serviceEstimate.getCustomer())
                .withDocumentNumber(docContent.getDocumentNumber())
                .withExpiryDate(docContent.getExpiryDate())
                .withNameOnDocument(docContent.getNameOnTheDocument())
                .withProvider(docContent.getProvider())
                .withInformation(docContent.getDescription())
                .withDocumentUrl(docContent.getDocumentURL())
                .withServiceVehicle(serviceEstimate.getServiceVehicle())
                .withMdVehicleDocumentType(mdDocumentType)
                .withDocumentContent(documentContent).build();
    }

    public DocumentContent fetchOrCreateDocumentContentEntity(DocContent docContent) {
        var documentContent = documentContentService.findByUUID(docContent.getUuid());
        if (StringUtils.isNoneBlank(docContent.getUuid()) && Objects.isNull(documentContent))
            throw new VehicleServicesException(String.format("Document %s is not found with UUID %s",
                    docContent.getDocumentName(), docContent.getUuid()));
        if (Objects.isNull(documentContent))
            if (StringUtils.isNoneBlank(docContent.getDocumentURL()))
                documentContent = documentContentService.constructDocumentContentEntityFromURL(docContent);
            else if (!Objects.isNull(docContent.getData()))
                documentContent = documentContentService.constructDocumentContentEntityFromEncodedString(docContent);
        return documentContent;
    }

    /**
     * Calculates regular maintenance estimate.
     *
     * @param estimateRequest
     * @param tenant
     * @return
     */
    public Map<String, List<RateCardItem>> calculateRegularMaintenanceEstimate(ServiceEstimateRequest estimateRequest
            , int tenant) {
        log.info(String.format("Identifying service type for vehicle %s",
                estimateRequest.getVehicleDetails().getVehicleRegNumber()));

        final int serviceType =
                serviceTypeCriteriaService.identifyServiceType(estimateRequest.getVehicleDetails().getPurchaseDate(),
                        estimateRequest.getVehicleDetails().getKmReading(), tenant);

        // Step #2 - user rate card  service to fetch rate card for the service type identified from step  # 1
        Map<String, List<RateCardItem>> rateCardItemsByService = rateCardService
                .fetchRateCardByTenantAndVehicleAndServiceType(tenant,
                        estimateRequest.getVehicleDetails().getVehicleModel(),
                        estimateRequest.getVehicleDetails().getFuelType(), serviceType);

        if (Objects.isNull(rateCardItemsByService) || rateCardItemsByService.isEmpty())
            throw new VehicleServicesException(
                    String.format("No rate card found for the given Tenant %s, Vehicle %s, " +
                                    "Variant %s " +
                                    "and service %s combination: ",
                            tenant,
                            estimateRequest.getVehicleDetails().getVehicleModel(),
                            estimateRequest.getVehicleDetails().getFuelType().name(),
                            estimateRequest.getServiceType().name()));

        return rateCardItemsByService;
    }

    /**
     * Creates ServiceEstimate entity from estimate request.
     *
     * @param estimateRequest
     * @return
     */
    public ServiceEstimate constructServiceEstimateEntityFrom(ServiceEstimateRequest estimateRequest,
                                                              final MdTenant mdTenant) {

        if (Objects.isNull(mdTenant))
            throw new VehicleServicesException("Tenant doesn't exist. Estimate cant be calculated.");

        Customer customer = customerFacade.fetchOrConstructCustomerEntity(estimateRequest.getCustomer(), mdTenant);

        ServiceVehicle serviceVehicle =
                vehicleFacade.fetchOrCreateServiceVehicleEntity(estimateRequest.getVehicleDetails(), customer,
                        mdTenant);
        serviceVehicle =
                vehicleFacade.addInsuranceDetailsToExistingVehicle(estimateRequest.getVehicleInsuranceDetails(),
                        serviceVehicle);

        ServiceEstimateBuilder serviceEstimateBuilder = ServiceEstimateBuilder.aServiceEstimate().withCustomer(customer)
                .withServiceVehicle(serviceVehicle).withPaymentType(estimateRequest.getPaymentType());

        if (!serviceVehicle.getVehicleKmTrackers().isEmpty())
            serviceEstimateBuilder.withVehicleKmTracker(
                    serviceVehicle.getVehicleKmTrackers().get(serviceVehicle.getVehicleKmTrackers().size() - 1));


        ServiceEstimate serviceEstimate = serviceEstimateBuilder.withServiceGroup(estimateRequest.getServiceType())
                .withMdTenant(mdTenant).build();
        customer.addServiceEstimateRequest(serviceEstimate);
        return serviceEstimate;
    }

    /**
     * Traverse through all rate card items and sums the cost.
     *
     * @param rateCardByService
     * @return
     */
    private Double calculateTotalEstimate(Map<String, List<RateCardItem>> rateCardByService) {
        return rateCardByService.values().stream()
                .map(rateCardItems -> rateCardItems.stream().map(RateCardItem::getRate).reduce(Double::sum)
                        .orElse(ZERO_DOUBLE))
                .reduce(Double::sum).orElse(ZERO_DOUBLE);
    }

    public List<ServiceEstimateResponseDTO> fetchEstimatesByOrgAndCustomerAndServiceGroup(String org,
                                                                                          String customerContactNumber,
                                                                                          ServiceGroup serviceGroup) {

        final var cleansePhoneNumber = phoneNumberUtil.cleansePhoneNumber(customerContactNumber);
        final var serviceEstimates = serviceEstimateRepository
                .findAll(ServiceEstimate.estimateByOrgIdentifierSpecification(org)
                                .and(ServiceEstimate.estimateByServiceGroupSpecification(serviceGroup)
                                        .and(ServiceEstimate.estimateByCustomerContactNumber(cleansePhoneNumber))),
                        Sort.by("createdDate").descending());

        if (CollectionUtils.isEmpty(serviceEstimates))
            return Collections.emptyList();

        return serviceEstimates.stream().map(ServiceEstimateResponseDTO::new).collect(Collectors.toList());
    }
}
