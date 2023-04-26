package com.automate.vehicleservices.service.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.vehicleservices.api.model.CustomerSearchRequest;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.CustomerTenant;
import com.automate.vehicleservices.entity.ServiceReminderFollowUp;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleInsurance;
import com.automate.vehicleservices.entity.specifications.CustomerSpecifications;
import com.automate.vehicleservices.entity.specifications.VehicleSpecifications;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.CustomerRepository;
import com.automate.vehicleservices.repository.CustomerTenantRepository;
import com.automate.vehicleservices.repository.ServiceReminderFollowUpActivityRepository;
import com.automate.vehicleservices.repository.ServiceReminderFollowUpRepository;
import com.automate.vehicleservices.repository.ServiceVehicleRepository;
import com.automate.vehicleservices.repository.VehicleInsuranceRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.SearchResponseDTO;

@Service
public class CustomerSearchFacade {

    private final CustomerRepository customerRepository;
    private final CustomerSpecifications customerSpecifications;
    private final VehicleSpecifications vehicleSpecifications;
    private final VehicleInsuranceSpecifications vehicleInsuranceSpecifications;
    private final ServiceVehicleRepository vehicleRepository;
    private final VehicleInsuranceRepository vehicleInsuranceRepository;
    private final CustomerTenantRepository customerTenantRepository;
    private final ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository;
    private final ServiceReminderFollowUpRepository serviceReminderFollowUpRepository;

    public CustomerSearchFacade(CustomerRepository customerRepository,
                                CustomerSpecifications customerSpecifications,
                                VehicleSpecifications vehicleSpecifications,
                                VehicleInsuranceSpecifications vehicleInsuranceSpecifications,
                                ServiceVehicleRepository vehicleRepository,
                                VehicleInsuranceRepository vehicleInsuranceRepository,
                                CustomerTenantRepository customerTenantRepository,
                                ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository,
                                ServiceReminderFollowUpRepository serviceReminderFollowUpRepository) {
        this.customerRepository = customerRepository;
        this.customerSpecifications = customerSpecifications;
        this.vehicleSpecifications = vehicleSpecifications;
        this.vehicleInsuranceSpecifications = vehicleInsuranceSpecifications;
        this.vehicleRepository = vehicleRepository;
        this.vehicleInsuranceRepository = vehicleInsuranceRepository;
        this.customerTenantRepository = customerTenantRepository;
        this.serviceReminderFollowUpActivityRepository=serviceReminderFollowUpActivityRepository;
        this.serviceReminderFollowUpRepository=serviceReminderFollowUpRepository;
    }

    @Transactional
    public PaginatedSearchResponse<SearchResponseDTO> search(CustomerSearchRequest customerSearchRequest, String tenant,
                                                             int page, int size) {

        // If only one search param exists then search on that respective field and table.
        if (!customerSearchRequest.atLeastOneSearchFieldExists())
            throw new VehicleServicesException("At least one search filed is required to perform search");

        if (customerSearchRequest.isSingleFieldSearch()) {
            //Perform single field search
            return singleFieldSearch(customerSearchRequest, tenant, page, size);
        }

        return compositeFieldSearch(customerSearchRequest, tenant, page, size);
    }

    private PaginatedSearchResponse<SearchResponseDTO> compositeFieldSearch(CustomerSearchRequest customerSearchRequest,
                                                                            String tenant, int page, int size) {
        return customerRepository.search(customerSearchRequest, page, size);
    }

    private Specification<CustomerTenant> byCustomerName(CustomerSearchRequest customerSearchRequest) {
        return
                Specification.where(customerSpecifications.firstNameContains(customerSearchRequest.getCustomerName())
                        .or(customerSpecifications.lastNameContains(customerSearchRequest.getCustomerName())));
    }

    private Specification<CustomerTenant> byCustomerId(CustomerSearchRequest customerSearchRequest) {
        return
                Specification.where(customerSpecifications.idEquals(customerSearchRequest.getCustomerId()));
    }

    private Specification<CustomerTenant> byContactNumber(CustomerSearchRequest customerSearchRequest) {
        return Specification
                .where(customerSpecifications.contactNumberContains(customerSearchRequest.getContactNumber()))
                .or(customerSpecifications.altContactNumberContains(customerSearchRequest.getContactNumber()));

    }

    private Specification<ServiceVehicle> byVehicleRegNumber(final String vehicleRegNumber, final String tenant) {
        return
                Specification.where(vehicleSpecifications
                        .vehicleRegNumberContains(vehicleRegNumber)
                        .and(vehicleSpecifications.tenantEquals(tenant)));


    }

    private Specification<ServiceVehicle> byEngineNumber(final String engineNumber, final String tenant) {
        return
                Specification
                        .where(vehicleSpecifications.engineNumberContains(engineNumber)
                                .and(vehicleSpecifications.tenantEquals(tenant)));

    }

    private Specification<ServiceVehicle> byChassisNumber(final String chassisNumber, final String tenant) {
        return
                Specification.where(vehicleSpecifications.chassisNumberContains(chassisNumber)
                        .and(vehicleSpecifications.tenantEquals(tenant)));


    }

    private Specification<ServiceVehicle> byVIN(final String vin, final String tenant) {
        return
                Specification.where(vehicleSpecifications.vinContains(vin)
                        .and(vehicleSpecifications.tenantEquals(tenant)));

    }

    private PaginatedSearchResponse<SearchResponseDTO> singleFieldSearch(CustomerSearchRequest customerSearchRequest,
                                                                         final String tenant, int page, int size) {
        Specification<CustomerTenant> customerSpecification = getCustomerSpecification(customerSearchRequest);
        if (Objects.nonNull(customerSpecification))
            return queryWithCustomerSpecification(customerSpecification, tenant, page, size);

        Specification<ServiceVehicle> vehicleSpecification = getVehicleSpecification(customerSearchRequest, tenant);
        if (Objects.nonNull(vehicleSpecification))
            return queryWithVehicleSpecification(vehicleSpecification, page, size);

        Specification<VehicleInsurance> vehiclePolicySpecification = getVehiclePolicySpecification(
                customerSearchRequest);
        if (Objects.nonNull(vehiclePolicySpecification))
            return queryWithVehicleInsuranceSpecification(vehiclePolicySpecification, page, size);
        return null;
    }

    private Specification<VehicleInsurance> getVehiclePolicySpecification(CustomerSearchRequest customerSearchRequest) {
        Specification<VehicleInsurance> vehiclePolicySpecification = null;
        if (StringUtils.isNotBlank(customerSearchRequest.getPolicyNumber()))
            vehiclePolicySpecification = byPolicyNumber(customerSearchRequest.getPolicyNumber());

        return vehiclePolicySpecification;
    }

    private Specification<VehicleInsurance> byPolicyNumber(String policyNumber) {
        return
                Specification.where(vehicleInsuranceSpecifications
                        .policyNumberContains(policyNumber));
    }

    private Specification<ServiceVehicle> getVehicleSpecification(CustomerSearchRequest customerSearchRequest,
                                                                  final String tenant) {
        Specification<ServiceVehicle> customerSpecification = null;
        if (StringUtils.isNoneBlank(customerSearchRequest.getVehicleRegNumber()))
            customerSpecification = byVehicleRegNumber(customerSearchRequest.getVehicleRegNumber(), tenant);
        else if (StringUtils.isNoneBlank(customerSearchRequest.getVin()))
            customerSpecification = byVIN(customerSearchRequest.getVin(), tenant);
        else if (StringUtils.isNotBlank(customerSearchRequest.getEngineNumber()))
            customerSpecification = byEngineNumber(customerSearchRequest.getEngineNumber(), tenant);
        else if (StringUtils.isNotBlank(customerSearchRequest.getChassis()))
            customerSpecification = byChassisNumber(customerSearchRequest.getChassis(), tenant);
        return customerSpecification;
    }

    private Specification<CustomerTenant> getCustomerSpecification(CustomerSearchRequest customerSearchRequest) {
        Specification<CustomerTenant> customerSpecification = null;
        if (StringUtils.isNoneBlank(customerSearchRequest.getCustomerName()))
            customerSpecification = byCustomerName(customerSearchRequest);
        else if (customerSearchRequest.getCustomerId() > 0) {
            customerSpecification = byCustomerId(customerSearchRequest);

        } else if (StringUtils.isNotBlank(customerSearchRequest.getContactNumber()))
            customerSpecification = byContactNumber(customerSearchRequest);
        return customerSpecification;
    }

    private PaginatedSearchResponse<SearchResponseDTO> queryWithCustomerSpecification(
            Specification<CustomerTenant> customerSpecification,
            final String tenant, int page, int size) {
        final var all = customerTenantRepository.findAll(customerSpecification, PageRequest.of(page, size));

        final var searchResponseDTOS = filterAndConstructToDTO(all.toList());
        return new PaginatedSearchResponse(all, searchResponseDTOS);
    }

    private List<SearchResponseDTO> filterAndConstructToDTO(List<CustomerTenant> all) {
        if (CollectionUtils.isEmpty(all))
            return Collections.emptyList();

        List<SearchResponseDTO> result = new ArrayList<>();
        for (CustomerTenant c : all) {
            if (CollectionUtils.isEmpty(c.getCustomer().getCustomerVehicles()))
                result.add(toSearchResponse(c.getCustomer()));
            else
                result.addAll(c.getCustomer().getCustomerVehicles().stream()
                        .map(this::toSearchResponse).collect(Collectors.toList()));
        }
        return result;
    }

    private SearchResponseDTO toSearchResponse(Customer customer) {
    	 LocalDate dueDate=null;
         String serviceType=null;
         String subServiceType=null;
         if(customer!=null) {
         	ServiceReminderFollowUp srfu=serviceReminderFollowUpRepository.findByCustomerId(customer.getId());
         	if(srfu!=null) {
         		//ServiceVehicle sv =srfu.getServiceVehicle();
         		serviceType=srfu.getServiceReminderDetails().getServiceReminder()
 						.getMdServiceType().getMdServiceCategory().getCategoryName();
         		subServiceType = srfu.getServiceReminderDetails().getServiceReminder().getMdServiceType()
						.getServiceName();
         	  Integer id=	srfu.getId();
         	 dueDate = serviceReminderFollowUpActivityRepository.findById(id).get().getEndDate();
         	}
         }
        return SearchResponseDTO.builder().customerId(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .contactNumber(customer.getContactNumber()).dueDate(dueDate).serviceType(serviceType).subServiceType(subServiceType).build();
    }

    private boolean isTenantExists(String tenant, Customer customer) {
        Set<CustomerTenant> customerTenants = customer.getCustomerTenants();
        if (CollectionUtils.isEmpty(customerTenants))
            return false;

        Optional<CustomerTenant> customerTenantOpt = customerTenants.stream()
                .filter(customerTenant -> Objects.nonNull(customerTenant.getMdTenant()) && StringUtils
                        .equalsIgnoreCase(tenant, customerTenant.getMdTenant().getTenantIdentifier())).findAny();

        return customerTenantOpt.isPresent();
    }

    private PaginatedSearchResponse<SearchResponseDTO> queryWithVehicleInsuranceSpecification(
            Specification<VehicleInsurance> vehicleSpecification, int page, int size) {
        final var all = vehicleInsuranceRepository.findAll(vehicleSpecification, PageRequest.of(page, size));
        final var searchResponseDTOS = all.stream().map(this::toSearchResponse).collect(Collectors.toList());
        return new PaginatedSearchResponse(all, searchResponseDTOS);

    }

    private SearchResponseDTO toSearchResponse(VehicleInsurance vehicleInsurance) {
    	LocalDate dueDate = null;
		String serviceType = null;
		String subServiceType = null;
		String serviceCenterName=null;
    	ServiceVehicle vehicle = vehicleInsurance.getServiceVehicle();
        if (vehicle.getCustomer() != null) {
			ServiceReminderFollowUp srfu = serviceReminderFollowUpRepository
					.findByCustomerId(vehicle.getCustomer().getId());
			if (srfu != null) {
				ServiceVehicle sv = srfu.getServiceVehicle();
				serviceType = srfu.getServiceReminderDetails().getServiceReminder().getMdServiceType()
						.getMdServiceCategory().getCategoryName();
				subServiceType = srfu.getServiceReminderDetails().getServiceReminder().getMdServiceType()
						.getServiceName();
				Integer id = srfu.getId();
				dueDate = vehicle.getDueDate();
				serviceCenterName=vehicle.getMdTenant().getTenantName();
			}
		}
        return SearchResponseDTO.builder()
                .vehicleRegNumber(vehicle.getRegNumber())
                .engineNumber(vehicle.getEngineNumber())
                .vin(vehicle.getVin())
                .model(vehicle.getModel())
                .customerId(vehicle.getCustomer().getId())
                .firstName(vehicle.getCustomer().getFirstName())
                .lastName(vehicle.getCustomer().getLastName())
                .contactNumber(vehicle.getCustomer().getContactNumber())
                .policyNumber(vehicleInsurance.getInsuranceIdentifier())
                .dueDate(dueDate).serviceType(serviceType).serviceCenterName(serviceCenterName).subServiceType(subServiceType).build();
    }

    private PaginatedSearchResponse<SearchResponseDTO> queryWithVehicleSpecification(
            Specification<ServiceVehicle> vehicleSpecification,
            int page, int size) {
        final var all = vehicleRepository.findAll(vehicleSpecification, PageRequest.of(page, size));

        final var searchResponseDTOS = all.stream().map(this::toSearchResponse).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(all, searchResponseDTOS);
    }

    private SearchResponseDTO toSearchResponse(ServiceVehicle vehicle) {
        return toSearchResponse(vehicle, null);
    }

	private SearchResponseDTO toSearchResponse(ServiceVehicle vehicle, Customer customer) {
		String policyNumber = StringUtils.EMPTY;
		LocalDate dueDate = vehicle.getDueDate();
		String serviceType = null;
		String subServiceType = null;
		if(vehicle.getServiceType()!=null) {
			serviceType = vehicle.getServiceType().getMdServiceCategory().getCategoryName();
			subServiceType = vehicle.getServiceType().getServiceName();
		}
		List<VehicleInsurance> vehicleInsurances = vehicle.getVehicleInsurances();
		String serviceCenterName=vehicle.getMdTenant().getTenantName();
		if (CollectionUtils.isNotEmpty(vehicleInsurances))
			policyNumber = vehicleInsurances.stream().findFirst().map(VehicleInsurance::getInsuranceIdentifier)
					.orElse(StringUtils.EMPTY);
		if (Objects.isNull(customer))
			customer = vehicle.getCustomer();
		return SearchResponseDTO.builder().vehicleRegNumber(vehicle.getRegNumber())
				.engineNumber(vehicle.getEngineNumber()).chassisNumber(vehicle.getChassisNumber()).vin(vehicle.getVin())
				.model(vehicle.getModel()).customerId(customer.getId()).firstName(customer.getFirstName())
				.lastName(customer.getLastName()).contactNumber(customer.getContactNumber()).policyNumber(policyNumber)
				.dueDate(dueDate).serviceType(serviceType).serviceCenterName(serviceCenterName).subServiceType(subServiceType).build();
	}

    public PaginatedSearchResponse<SearchResponseDTO> list(String tenant, final int page, final int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        final var all = customerTenantRepository.findByMdTenant_TenantIdentifier(tenant, pageable);
        final var searchResponseDTOS = filterAndConstructToDTO(all.toList());
        return new PaginatedSearchResponse<>(all, searchResponseDTOS);

    }


}
