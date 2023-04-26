package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.VehicleInsuranceRequest;
import com.automate.vehicleservices.api.model.VehicleServiceHistoryRequest;
import com.automate.vehicleservices.api.model.VehicleWarrantyRequest;
import com.automate.vehicleservices.api.model.v1.AddressRequestV1;
import com.automate.vehicleservices.api.model.v1.CustomerRequestV1;
import com.automate.vehicleservices.api.model.v1.VehicleDetailsV1;
import com.automate.vehicleservices.api.model.v1.VehicleHistoryRequest;
import com.automate.vehicleservices.api.model.v1.VehicleServiceHistoryRequestV1;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleInsurance;
import com.automate.vehicleservices.entity.VehicleServiceHistory;
import com.automate.vehicleservices.entity.VehicleWarranty;
import com.automate.vehicleservices.entity.builder.VehicleInsuranceBuilder;
import com.automate.vehicleservices.entity.builder.VehicleServiceHistoryBuilder;
import com.automate.vehicleservices.entity.builder.VehicleWarrantyBuilder;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.entity.enums.WarrantyStatus;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.VehicleServiceHistoryRepository;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.repository.dtoprojection.VehicleServiceHistoryDTO;
import com.automate.vehicleservices.service.facade.CustomerFacade;
import com.automate.vehicleservices.service.facade.VehicleFacade;
import com.automate.vehicleservices.service.facade.v1.CustomerFacadeV1;
import com.automate.vehicleservices.service.facade.v1.VehicleFacadeV1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Service
@Slf4j
public class VehicleServiceHistoryService {

    private final VehicleServiceHistoryRepository vehicleServiceHistoryRepository;
    private final MdTenantService tenantService;
    private final CrudService crudService;
    private final VehicleFacade vehicleFacade;
    private final VehicleFacadeV1 vehicleFacadeV1;
    private final CustomerFacade customerFacade;
    private final CustomerFacadeV1 customerFacadeV1;
    private final CustomerService customerService;

    private final ServiceVehicleService vehicleService;

    public VehicleServiceHistoryService(VehicleServiceHistoryRepository vehicleServiceHistoryRepository,
                                        MdTenantService tenantService, CrudService crudService,
                                        VehicleFacade vehicleFacade,
                                        VehicleFacadeV1 vehicleFacadeV1, CustomerFacade customerFacade, CustomerFacadeV1 customerFacadeV1, CustomerService customerService, ServiceVehicleService vehicleService) {
        this.vehicleServiceHistoryRepository = vehicleServiceHistoryRepository;
        this.tenantService = tenantService;
        this.crudService = crudService;
        this.vehicleFacade = vehicleFacade;
        this.vehicleFacadeV1 = vehicleFacadeV1;
        this.customerFacade = customerFacade;
        this.customerFacadeV1 = customerFacadeV1;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    public List<VehicleServiceHistory> vehicleServiceHistory(final String vehicleRegNumber) {
        return vehicleServiceHistoryRepository.findByServiceVehicle_RegNumberOrderByServiceDateDesc(vehicleRegNumber);
    }

    /**
     * Creates new vehicle service history.
     *
     * @param vehicleServiceHistoryRequest
     * @return
     */
    public VehicleServiceHistoryDTO createServiceHistory(VehicleServiceHistoryRequest vehicleServiceHistoryRequest,
                                                         final String tenantIdentifier) {
        final var contactNumber = vehicleServiceHistoryRequest.getCustomer().getContactNumber();
        List<Customer> customers = customerService.findByContactNumber(contactNumber);

        if (CollectionUtils.isNotEmpty(customers)) {
            throw new VehicleServicesException(String.format("Customer exists already with the same contact number %s",
                    contactNumber));
        }
        // ten
        final var tenant = tenantService.tenantByIdentifier(tenantIdentifier);

        if (Objects.isNull(tenant))
            throw new VehicleServicesException("Tenant is not found. Tenant is mandatory for service history " +
                    "information");
        // Customer
        Customer customer = customerFacade.fetchOrConstructCustomerEntity(vehicleServiceHistoryRequest.getCustomer(),
                tenant);

        // Vehicle
        vehicleServiceHistoryRequest.getVehicleDetails()
                .setKmReading(vehicleServiceHistoryRequest.getKmReadingAtService());
        final var serviceVehicle =
                vehicleFacade.fetchOrCreateServiceVehicleEntity(vehicleServiceHistoryRequest.getVehicleDetails(),
                        customer, tenant);


        // Service type
        final var serviceTypeId = vehicleServiceHistoryRequest.getServiceType();
        MdServiceType serviceType = null;
        if (serviceTypeId > 0) {
            serviceType = crudService.findById(serviceTypeId, MdServiceType.class).orElse(null);
        }

        // Construct entity
        final var vehicleServiceHistory = VehicleServiceHistoryBuilder.aVehicleServiceHistory()
                .withKmReading(vehicleServiceHistoryRequest.getKmReadingAtService())
                .withServiceVehicle(serviceVehicle)
                .withMdServiceType(serviceType)
                .withServiceAmount(vehicleServiceHistoryRequest.getServiceAmount())
                .withServiceCenter(vehicleServiceHistoryRequest.getServiceCenter())
                .withServiceDate(vehicleServiceHistoryRequest.getServiceDate())
                .withServiceManager(vehicleServiceHistoryRequest.getServiceManager())
                .withComplaintStatus(vehicleServiceHistoryRequest.getComplaintStatus())
                .withDealerName(vehicleServiceHistoryRequest.getDealerName())
                .withDealerLocation(vehicleServiceHistoryRequest.getDealerLocation())
                .withLastServiceFeedback(vehicleServiceHistoryRequest.getLastServiceFeedback())
                .withReasonForComplaint(vehicleServiceHistoryRequest.getReasonForComplaint())
                .withMdTenant(tenant)
                .withRemarks(vehicleServiceHistoryRequest.getInformation())
                .build();

        // Persist
        final var save = crudService.save(vehicleServiceHistory);

        log.info(String.format("Vehicle service history created for vehicle %s", serviceVehicle.getRegNumber()));
        return new VehicleServiceHistoryDTO(save);
    }

    @Transactional
    public List<VehicleServiceHistoryDTO> fetchServiceHistory(String vehicleRegNumber) {
        final var vehicleServiceHistories = vehicleServiceHistoryRepository
                .findByServiceVehicle_RegNumberOrderByServiceDateDesc(vehicleRegNumber);

        return getVehicleServiceHistoryDTOS(vehicleServiceHistories);
    }

    @Transactional
    public List<VehicleServiceHistoryDTO> fetchServiceHistoryByVehicleCategory(ServiceVehicleDTO serviceVehicle,
                                                                               ServiceGroup serviceGroup) {
        List<VehicleServiceHistory> serviceHistoryList = vehicleServiceHistoryRepository
                .findByServiceVehicle_RegNumberAndMdServiceType_MdTenant_TenantIdentifierAndMdServiceType_MdServiceCategory_ServiceGroupOrderByServiceDateDesc(
                        serviceVehicle.getRegNumber(), serviceVehicle.getTenantId(), serviceGroup);


        return getVehicleServiceHistoryDTOS(serviceHistoryList);
    }

    private List<VehicleServiceHistoryDTO> getVehicleServiceHistoryDTOS(
            List<VehicleServiceHistory> serviceHistoryList) {
        if (CollectionUtils.isEmpty(serviceHistoryList))
            return Collections.emptyList();

        return serviceHistoryList.stream().map(VehicleServiceHistoryDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public Object createServiceHistoryV1(VehicleServiceHistoryRequestV1 vehicleServiceHistoryRequest,
                                         final String tenantIdentifier) {
        final var contactNumber = vehicleServiceHistoryRequest.getCustomer().getContactNumber();
        List<Customer> customers = customerService.findByContactNumber(contactNumber);

        if (CollectionUtils.isNotEmpty(customers)) {
            throw new VehicleServicesException(String.format("Customer exists already with the same contact number %s",
                    contactNumber));
        }
        // ten
        final var tenant = tenantService.tenantByIdentifier(tenantIdentifier);

        if (Objects.isNull(tenant))
            throw new VehicleServicesException("Tenant is not found. Tenant is mandatory for service history " +
                    "information");
        // Customer
        Customer customer = customerFacadeV1.fetchOrConstructCustomerEntity(vehicleServiceHistoryRequest.getCustomer(),
                tenant);

        // Vehicle

        final var serviceVehicle =
                vehicleFacadeV1.fetchOrCreateServiceVehicleEntity(vehicleServiceHistoryRequest.getVehicleDetails(),
                        customer, tenant);


        if (Objects.nonNull(vehicleServiceHistoryRequest.getVehicleHistoryRequest())) {
            VehicleHistoryRequest historyRequest = vehicleServiceHistoryRequest.getVehicleHistoryRequest();
            // Service type
            final var serviceTypeId = historyRequest.getServiceType();
            MdServiceType serviceType = null;
            if (serviceTypeId > 0) {
                serviceType = crudService.findById(serviceTypeId, MdServiceType.class).orElse(null);
            }

            // Construct entity
            final var vehicleServiceHistory = VehicleServiceHistoryBuilder.aVehicleServiceHistory()
                    .withServiceManager(historyRequest.getServiceManager())
                    .withComplaintStatus(historyRequest.getComplaintStatus())
                    .withLastServiceFeedback(historyRequest.getLastServiceFeedback())
                    .withReasonForComplaint(historyRequest.getReasonForComplaint())
                    .withDealerName(historyRequest.getDealerName())
                    .withDealerLocation(historyRequest.getDealerLocation())
                    .withServiceDate(historyRequest.getServiceDate())
                    .withKmReading(historyRequest.getKmReadingAtService())
                    .withServiceCenter(historyRequest.getServiceCenter())
                    .withServiceAmount(historyRequest.getServiceAmount())
                    .withMdServiceType(serviceType)
                    .withServiceVehicle(serviceVehicle)
                    .withMdTenant(tenant)
                    .build();

            serviceVehicle.setVehicleServiceHistories(Collections.singletonList(vehicleServiceHistory));
        }

        if (Objects.nonNull(vehicleServiceHistoryRequest.getInsuranceRequest())) {
            VehicleInsuranceRequest insuranceRequest = vehicleServiceHistoryRequest.getInsuranceRequest();
            final var vehicleInsurance = VehicleInsuranceBuilder.aVehicleInsurance()
                    .withEndDate(insuranceRequest.getEndDate())
                    .withStartDate(insuranceRequest.getStartDate())
                    .withInsuranceAmount(insuranceRequest.getInsuranceAmount())
                    .withProvider(insuranceRequest.getVendor())
                    .withInsuranceIdentifier(insuranceRequest.getInsuranceIdentifier())
                    .withCustomer(customer)
                    .withServiceVehicle(serviceVehicle).build();
            serviceVehicle.setVehicleInsurances(Collections.singletonList(vehicleInsurance));
        }

        if (Objects.nonNull(vehicleServiceHistoryRequest.getWarrantyRequests()) && !vehicleServiceHistoryRequest.getWarrantyRequests().isEmpty()) {
            List<VehicleWarrantyRequest> warrantyRequestList = vehicleServiceHistoryRequest.getWarrantyRequests();
            List<VehicleWarranty> warrantyList = warrantyRequestList.stream().map(warrantyRequest -> VehicleWarrantyBuilder.aVehicleWarranty()
                    .withExpiryDate(warrantyRequest.getExpiryDate())
                    .withStartDate(warrantyRequest.getStartDate())
                    .withAmountPaid(warrantyRequest.getAmountPaid())
                    .withWarrantyTpe(warrantyRequest.getWarrantyType())
                    .withAmcName(warrantyRequest.getAmc_name())
                    .withEwName(warrantyRequest.getEwName())
                    .withFastagStatus(warrantyRequest.getFastagStatus())
                    .withOemPeriod(warrantyRequest.getOemPeriod())
                    .withStatus(WarrantyStatus.ACTIVE)
                    .withMdTenant(tenant)
                    .withNumber(warrantyRequest.getNumber())
                    .withServiceVehicle(serviceVehicle).build()
            ).collect(Collectors.toList());

            serviceVehicle.setVehicleWarranties(warrantyList);
        }

        // Persist
        final var save = crudService.save(serviceVehicle);

        log.info(String.format("Vehicle service history created for vehicle %s", serviceVehicle.getRegNumber()));
//        return new VehicleServiceHistoryDTO(save);
        return "Success!!";
    }

    @Transactional
    public Object UpdateServiceHistoryV1(VehicleServiceHistoryRequestV1 vehicleServiceHistoryRequest, Integer tenantIdentifier) {

        // ten
        final var tenant = tenantService.findByMasterIdentifier(tenantIdentifier);

        if (Objects.isNull(tenant))
            throw new VehicleServicesException("Tenant is not found. Tenant is mandatory for service history " +
                    "information");
        // Customer
        Customer customer = customerFacadeV1.getUpdateCustomerEntity(vehicleServiceHistoryRequest.getCustomer(),
                tenant);

        // Vehicle

        final var serviceVehicle =
                vehicleFacadeV1.fetchOrUpdateServiceVehicleEntity(vehicleServiceHistoryRequest.getVehicleDetails(),
                        customer, tenant);

        if (Objects.nonNull(vehicleServiceHistoryRequest.getVehicleHistoryRequest())) {
            VehicleHistoryRequest historyRequest = vehicleServiceHistoryRequest.getVehicleHistoryRequest();
            // Service type
            final var serviceTypeId = historyRequest.getServiceType();
            MdServiceType serviceType = null;
            if (serviceTypeId > 0) {
                serviceType = crudService.findById(serviceTypeId, MdServiceType.class).orElse(null);
            }

            // Construct entity
            final var vehicleServiceHistory = VehicleServiceHistoryBuilder.aVehicleServiceHistory()
                    .withServiceManager(historyRequest.getServiceManager())
                    .withComplaintStatus(historyRequest.getComplaintStatus())
                    .withLastServiceFeedback(historyRequest.getLastServiceFeedback())
                    .withReasonForComplaint(historyRequest.getReasonForComplaint())
                    .withDealerName(historyRequest.getDealerName())
                    .withDealerLocation(historyRequest.getDealerLocation())
                    .withServiceDate(historyRequest.getServiceDate())
                    .withKmReading(historyRequest.getKmReadingAtService())
                    .withServiceCenter(historyRequest.getServiceCenter())
                    .withServiceAmount(historyRequest.getServiceAmount())
                    .withMdServiceType(serviceType)
                    .withServiceVehicle(serviceVehicle)
                    .withMdTenant(tenant)
                    .build();
            vehicleServiceHistory.setId(String.valueOf(historyRequest.getId()));

            serviceVehicle.setVehicleServiceHistories(Collections.singletonList(vehicleServiceHistory));
        }

        if (Objects.nonNull(vehicleServiceHistoryRequest.getInsuranceRequest())) {
            VehicleInsuranceRequest insuranceRequest = vehicleServiceHistoryRequest.getInsuranceRequest();
            final var vehicleInsurance = VehicleInsuranceBuilder.aVehicleInsurance()
                    .withEndDate(insuranceRequest.getEndDate())
                    .withStartDate(insuranceRequest.getStartDate())
                    .withInsuranceAmount(insuranceRequest.getInsuranceAmount())
                    .withProvider(insuranceRequest.getVendor())
                    .withInsuranceIdentifier(insuranceRequest.getInsuranceIdentifier())
                    .withCustomer(customer)
                    .withServiceVehicle(serviceVehicle).build();
            vehicleInsurance.setId(insuranceRequest.getId());
            serviceVehicle.setVehicleInsurances(Collections.singletonList(vehicleInsurance));
        }

        if (Objects.nonNull(vehicleServiceHistoryRequest.getWarrantyRequests()) && !vehicleServiceHistoryRequest.getWarrantyRequests().isEmpty()) {
            List<VehicleWarrantyRequest> warrantyRequestList = vehicleServiceHistoryRequest.getWarrantyRequests();
            List<VehicleWarranty> warrantyList = warrantyRequestList.stream().map(warrantyRequest -> VehicleWarrantyBuilder.aVehicleWarranty()
                    .withExpiryDate(warrantyRequest.getExpiryDate())
                    .withStartDate(warrantyRequest.getStartDate())
                    .withAmountPaid(warrantyRequest.getAmountPaid())
                    .withWarrantyTpe(warrantyRequest.getWarrantyType())
                    .withAmcName(warrantyRequest.getAmc_name())
                    .withEwName(warrantyRequest.getEwName())
                    .withFastagStatus(warrantyRequest.getFastagStatus())
                    .withOemPeriod(warrantyRequest.getOemPeriod())
                    .withStatus(WarrantyStatus.ACTIVE)
                    .withMdTenant(tenant)
                    .withNumber(warrantyRequest.getNumber())
                    .withId(warrantyRequest.getId())
                    .withServiceVehicle(serviceVehicle).build()
            ).collect(Collectors.toList());

            serviceVehicle.setVehicleWarranties(warrantyList);
        }

        // Persist
        final var save = crudService.save(serviceVehicle);

        return "Updated!!";
    }

    @Transactional
    public Object getCustomerWithAllSectionDetails(String regNumber, int tenantId) {
        Map<String, Object> responseMap = new HashMap<>();
//        Vehicle Section
        ServiceVehicle serviceVehicle = vehicleService.findByVehicleRegNumber(regNumber);
        if (Objects.nonNull(serviceVehicle)) {
            VehicleDetailsV1 vehicleDetailsV1 = getVehicleDetailsV1(serviceVehicle);
            responseMap.put("vehicleDetail", vehicleDetailsV1);

//        Customer Section
            Customer customer = serviceVehicle.getCustomer();
            List<AddressRequestV1> addressRequestV1s = getAddressRequestV1(customer);
            CustomerRequestV1 customerRequestV1 = getCustomerDetailsV1(customer, addressRequestV1s);
            responseMap.put("customerDetail", customerRequestV1);

//        History Section
            if (!serviceVehicle.getVehicleServiceHistories().isEmpty() && Objects.nonNull(serviceVehicle.getVehicleServiceHistories().get(0))) {
                VehicleServiceHistory vehicleServiceHistory = serviceVehicle.getVehicleServiceHistories().get(0);
                VehicleHistoryRequest vehicleHistoryRequest = getVehicleHistoryRequest(vehicleServiceHistory);
                responseMap.put("historyDetail", vehicleHistoryRequest);
            }

//        Insurance Section
            if (!serviceVehicle.getVehicleInsurances().isEmpty() && Objects.nonNull(serviceVehicle.getVehicleInsurances().get(0))) {
                VehicleInsurance vehicleInsurance = serviceVehicle.getVehicleInsurances().get(0);
                VehicleInsuranceRequest vehicleInsuranceRequest = getVehicleInsuranceRequest(vehicleInsurance);
                responseMap.put("insuranceDetail", vehicleInsuranceRequest);
            }

//        Warranty Section
            if (!serviceVehicle.getVehicleWarranties().isEmpty()) {
                List<VehicleWarrantyRequest> vehicleWarrantyRequest = serviceVehicle.getVehicleWarranties().stream().map(data -> VehicleWarrantyRequest.builder()
                        .id(data.getId())
                        .startDate(data.getStartDate())
                        .expiryDate(data.getExpiryDate())
                        .amountPaid(data.getAmountPaid())
                        .oemPeriod(data.getOemPeriod())
                        .ewName(data.getEwName())
                        .amc_name(data.getAmc_name())
                        .warrantyType(data.getWarrantyTpe())
                        .number(data.getNumber())
                        .build()

                ).collect(Collectors.toList());
                responseMap.put("warrantyDetail", vehicleWarrantyRequest);
            }

        } else {
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Vehicle not found!");
        }

        return responseMap;


    }

    private VehicleInsuranceRequest getVehicleInsuranceRequest(VehicleInsurance vehicleInsurance) {
        return VehicleInsuranceRequest.builder()
                .endDate(vehicleInsurance.getEndDate())
                .id(vehicleInsurance.getId())
                .insuranceAmount(vehicleInsurance.getInsuranceAmount())
                .insuranceIdentifier(vehicleInsurance.getInsuranceIdentifier())
                .startDate(vehicleInsurance.getStartDate())
                .vendor(vehicleInsurance.getProvider())
                .build();
    }

    private VehicleHistoryRequest getVehicleHistoryRequest(VehicleServiceHistory vehicleServiceHistory) {
        return VehicleHistoryRequest.builder()
                .id(Integer.parseInt(vehicleServiceHistory.getId()))
                .serviceManager(vehicleServiceHistory.getServiceManager())
                .complaintStatus(vehicleServiceHistory.getComplaintStatus())
                .lastServiceFeedback(vehicleServiceHistory.getLastServiceFeedback())
                .reasonForComplaint(vehicleServiceHistory.getReasonForComplaint())
                .dealerName(vehicleServiceHistory.getDealerName())
                .dealerLocation(vehicleServiceHistory.getDealerLocation())
                .serviceDate(vehicleServiceHistory.getServiceDate())
                .kmReadingAtService(vehicleServiceHistory.getKmReading())
                .serviceCenter(vehicleServiceHistory.getServiceCenter())
                .serviceAmount(vehicleServiceHistory.getServiceAmount())
                .serviceType(Objects.nonNull(vehicleServiceHistory.getMdServiceType()) ? vehicleServiceHistory.getMdServiceType().getMdServiceCategory().getId() : 0)
                .subServiceType(Objects.nonNull(vehicleServiceHistory.getMdServiceType()) ? vehicleServiceHistory.getMdServiceType().getId() : 0)
                .build();
    }

    private List<AddressRequestV1> getAddressRequestV1(Customer customer) {
        return customer.getCustomerAddresses().stream().map(data -> AddressRequestV1.builder()
                .id(data.getId())
                .pin(data.getPin())
                .houseNo(data.getHouseNo())
                .street(data.getStreet())
                .villageOrTown(data.getVillageOrTown())
                .city(data.getCity())
                .mandalOrTahasil(data.getMandalOrTahasil())
                .district(data.getDistrict())
                .state(data.getState())
                .isUrban(data.getIsUrban())
                .build()
        ).collect(Collectors.toList());
    }

    private CustomerRequestV1 getCustomerDetailsV1(Customer customer, List<AddressRequestV1> addressRequestV1s) {
        return CustomerRequestV1.builder()
                .customerType(customer.getCustomerType())
                .addresses(addressRequestV1s)
                .age(customer.getAge())
                .contactNumber(customer.getContactNumber())
                .alternateContactNumber(customer.getAltContactNumber())
                .id(customer.getId())
                .dateOfArrival(customer.getDateOfArrival())
                .dateOfBirth(customer.getDateOfBirth())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .gender(customer.getGender())
                .lastName(customer.getLastName())
                .leadSource(customer.getLeadSource().getId())
                .occupation(customer.getOccupation())
                .parentLeadSource(customer.getLeadSource().getParentId().getId())
                .relationName(customer.getRelationName())
                .salutation(customer.getSalutation())
                .build();
    }

    private VehicleDetailsV1 getVehicleDetailsV1(ServiceVehicle serviceVehicle) {
        return VehicleDetailsV1.builder()
                .chassisNumber(serviceVehicle.getChassisNumber())
                .color(serviceVehicle.getColor())
                .currentKmReading(serviceVehicle.getCurrentKmReading())
                .engineNumber(serviceVehicle.getEngineNumber())
                .id(serviceVehicle.getId())
                .fuelType(serviceVehicle.getFuelType())
                .isFastag(serviceVehicle.getIsFastag())
                .makingMonth(serviceVehicle.getMakingMonth())
                .makingYear(serviceVehicle.getMakingYear())
                .purchaseDate(serviceVehicle.getPurchaseDate())
                .sellingDealer(serviceVehicle.getSellingDealer())
                .sellingLocation(serviceVehicle.getSellingLocation())
                .vehicleMake(serviceVehicle.getVehicleMake())
                .vehicleModel(serviceVehicle.getModel())
                .transmisionType(serviceVehicle.getTransmisionType())
                .vehicleRegNumber(serviceVehicle.getRegNumber())
                .variant(serviceVehicle.getVariant())
                .vin(serviceVehicle.getVin())
                .build();
    }
}
