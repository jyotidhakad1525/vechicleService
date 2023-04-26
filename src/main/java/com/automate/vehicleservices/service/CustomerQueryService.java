package com.automate.vehicleservices.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.vehicleservices.api.model.customerquery.CustomerQueryRequest;
import com.automate.vehicleservices.api.model.customerquery.CustomerQuerySearchRequest;
import com.automate.vehicleservices.entity.CustomerQuery;
import com.automate.vehicleservices.entity.builder.CustomerQueryBuilder;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryDepartment;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryPurpose;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryStatus;
import com.automate.vehicleservices.entity.specifications.CustomerQuerySpecifications;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.CustomerQueryRepository;
import com.automate.vehicleservices.service.dto.CustomerQueryDTO;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@Service
public class CustomerQueryService {

    private final CustomerService customerService;

    private final ServiceVehicleService serviceVehicleService;

    private final CustomerQueryRepository customerQueryRepository;

    private final CustomerQuerySpecifications customerQuerySpecifications;
    private static List<CustomerQueryDTO> customerQueryList = new ArrayList<>();
    private static PodamFactory factory = new PodamFactoryImpl();
    static {
    	CustomerQueryDTO record = new CustomerQueryDTO();
    	record.setActionPlan("no plan");
    	record.setCreAssigned("admin");
    	record.setCreatedBy("admin");
    	record.setCreatedDate(LocalDateTime.now());
    	record.setCreRemarks("remarks  ");
    	record.setCustomerId(369);
    	record.setCustomerQueryEnquiryDepartment(factory.manufacturePojoWithFullData(CustomerQueryEnquiryDepartment.class));
//    	CustomerQueryDTO query = factory.manufacturePojoWithFullData(CustomerQueryDTO.class);
    	record.setCustomerQueryEnquiryPurpose(CustomerQueryEnquiryPurpose.VEHICLE_ACCESSORIES);
    	record.setCustomerQueryEnquiryStatus(CustomerQueryEnquiryStatus.OPEN);
    	record.setId("123456");
    	record.setVehicleRegNumber("mhqt5096");
    	record.setQuery("my query");
    	customerQueryList.add(record);
    }

    public CustomerQueryService(CustomerService customerService, ServiceVehicleService serviceVehicleService,
                                CustomerQueryRepository customerQueryRepository,
                                CustomerQuerySpecifications customerQuerySpecifications) {
        this.customerService = customerService;
        this.serviceVehicleService = serviceVehicleService;
        this.customerQueryRepository = customerQueryRepository;
        this.customerQuerySpecifications = customerQuerySpecifications;
    }

    @Transactional
    public CustomerQueryDTO createCustomerQuery(CustomerQueryRequest customerQueryRequest) {
    	System.out.println("<<<<<<<<<<<<<CustomerID In Service>>>>>>>>>>>"+customerQueryRequest.getCustomerId());
        final CustomerQuery customerQuery = CustomerQueryBuilder.aCustomerQuery()
                .withActionPlan(customerQueryRequest.getActionPlan())
                .withCustomer(customerService.getCustomerOrThrowExceptionIfNotExists(customerQueryRequest.getCustomerId()))
                .withServiceVehicle(serviceVehicleService.findByVehicleRegNumberOrThrowException(customerQueryRequest.getVehicleRegNumber()))
                .withCreRemarks(customerQueryRequest.getCrmRemarks())
                .withCustomerQueryEnquiryDepartment(CustomerQueryEnquiryDepartment.valueOf(customerQueryRequest.getEnquiryDepartment()))
                .withCustomerQueryEnquiryStatus(CustomerQueryEnquiryStatus.OPEN)
                .withCustomerQueryEnquiryPurpose(CustomerQueryEnquiryPurpose.valueOf(customerQueryRequest.getPurpose()))
                .withCreAssigned(customerQueryRequest.getAssignedTo())
                .withQuery(customerQueryRequest.getQuery())
                .withCreatedBy(customerQueryRequest.getCreatedBy())
                .build();

        final CustomerQuery save = customerQueryRepository.save(customerQuery);
        return new CustomerQueryDTO(save);
    }
    public CustomerQueryDTO createCustomerQueryBilla(CustomerQueryRequest customerQueryRequest) {
    	Random random = new Random();
    	random.nextInt(10000);
    	System.out.println("<<<<<<<<<<<<<CustomerID In Service>>>>>>>>>>>"+customerQueryRequest.getCustomerId());
    	final CustomerQuery customerQuery = CustomerQueryBuilder.aCustomerQuery()
    			.withActionPlan(customerQueryRequest.getActionPlan())
    			.withCustomer(customerService.getCustomerOrThrowExceptionIfNotExists(customerQueryRequest.getCustomerId()))
    			.withServiceVehicle(serviceVehicleService.findByVehicleRegNumberOrThrowException(customerQueryRequest.getVehicleRegNumber()))
    			.withCreRemarks(customerQueryRequest.getCrmRemarks())
    			.withCustomerQueryEnquiryDepartment(CustomerQueryEnquiryDepartment.valueOf(customerQueryRequest.getEnquiryDepartment()))
    			.withCustomerQueryEnquiryStatus(CustomerQueryEnquiryStatus.OPEN)
    			.withCustomerQueryEnquiryPurpose(CustomerQueryEnquiryPurpose.valueOf(customerQueryRequest.getPurpose()))
    			.withCreAssigned(customerQueryRequest.getAssignedTo())
    			.withQuery(customerQueryRequest.getQuery())
    			.withCreatedBy(customerQueryRequest.getCreatedBy())
    			.withId(String.valueOf(random.nextInt(10000)))
    			.build();
    	
//    	final CustomerQuery save = customerQueryRepository.save(customerQuery);
    	CustomerQueryDTO customerQueryDTO = new CustomerQueryDTO(customerQuery);
    	customerQueryList.add(customerQueryDTO);
    	return customerQueryDTO;
    }

    @Transactional
    public CustomerQueryDTO updateCustomerQuery(final int id, CustomerQueryRequest customerQueryRequest) {

        CustomerQuery customerQuery =
                customerQueryRepository.findById(id).orElseThrow(() -> new VehicleServicesException(String.format(
                        "Vehicle not found with reg number %d", id)));

        customerQuery.setActionPlan(customerQueryRequest.getActionPlan());
        customerQuery.setCustomer(customerService.getCustomerOrThrowExceptionIfNotExists(customerQueryRequest.getCustomerId()));
        customerQuery.setServiceVehicle(serviceVehicleService.findByVehicleRegNumberOrThrowException(customerQueryRequest.getVehicleRegNumber()));
        customerQuery.setCreRemarks(customerQueryRequest.getCrmRemarks());
        customerQuery.setCustomerQueryEnquiryDepartment(CustomerQueryEnquiryDepartment.valueOf(customerQueryRequest.getEnquiryDepartment()));
        customerQuery.setCustomerQueryEnquiryStatus(CustomerQueryEnquiryStatus.valueOf(customerQueryRequest.getStatus()));
        customerQuery.setCustomerQueryEnquiryPurpose(CustomerQueryEnquiryPurpose.valueOf(customerQueryRequest.getPurpose()));
        customerQuery.setAssignedTo(customerQueryRequest.getAssignedTo());
        customerQuery.setQuery(customerQueryRequest.getQuery());

        final CustomerQuery save = customerQueryRepository.save(customerQuery);
        return new CustomerQueryDTO(save);
    }

    @Transactional
    public void deleteCustomerQuery(Integer id) {
        final boolean exists = customerQueryRepository.existsById(id);
        if (!exists)
            throw new VehicleServicesException(String.format("Vehicle not found with reg number %d", id));

        customerQueryRepository.deleteById(id);
    }

    @Transactional
    public PaginatedSearchResponse searchCustomerQueries(CustomerQuerySearchRequest customerSearchRequest,
                                                         String tenant,
                                                         int page, int size) {

        Specification<CustomerQuery> rootSpecification = getCustomerQuerySpecification(customerSearchRequest);

        final var all = customerQueryRepository.findAll(rootSpecification, PageRequest.of(page, size, Sort.by(
                "createdDate").descending()));

        final List<CustomerQueryDTO> searchResponseDTOS = constructQueryDTOList(all.toList());
        return new PaginatedSearchResponse(all, searchResponseDTOS);
    }
    
    public PaginatedSearchResponse searchCustomerQueriesBilla(CustomerQuerySearchRequest customerSearchRequest,
            String tenant,
            int page, int size) {
//		
//		Specification<CustomerQuery> rootSpecification = getCustomerQuerySpecification(customerSearchRequest);
//		
//		final var all = customerQueryRepository.findAll(rootSpecification, PageRequest.of(page, size, Sort.by(
//		"createdDate").descending()));
//		
//		final List<CustomerQueryDTO> searchResponseDTOS = constructQueryDTOList(all.toList());
		return new PaginatedSearchResponse(customerQueryList,1, customerQueryList.size(), 10);
		
		}

    private Specification<CustomerQuery> getCustomerQuerySpecification(CustomerQuerySearchRequest customerSearchRequest) {
        Specification<CustomerQuery> rootSpecification = null;
        if (StringUtils.isNotBlank(customerSearchRequest.getCreatedBy())) {
            rootSpecification = appendSpecification(rootSpecification,
                    customerQuerySpecifications.hasCreatedBy(customerSearchRequest.getCreatedBy()));
        }

        if (StringUtils.isNotBlank(customerSearchRequest.getAssignedTo())) {
            rootSpecification = appendSpecification(rootSpecification,
                    customerQuerySpecifications.hasAssignedTo(customerSearchRequest.getAssignedTo()));
        }

        if (customerSearchRequest.getId() > 0) {
            rootSpecification = appendSpecification(rootSpecification,
                    customerQuerySpecifications.idEqualsTo(customerSearchRequest.getId()));
        }
        return rootSpecification;
    }

    private List<CustomerQueryDTO> constructQueryDTOList(List<CustomerQuery> customerQueries) {
        if (CollectionUtils.isEmpty(customerQueries))
            return Collections.emptyList();
        return customerQueries.stream().map(CustomerQueryDTO::new).collect(Collectors.toList());
    }


    private Specification<CustomerQuery> appendSpecification(Specification<CustomerQuery> rootSpecification,
                                                             Specification<CustomerQuery> attributeSpecification) {
        if (Objects.isNull(attributeSpecification))
            return rootSpecification;

        if (Objects.isNull(rootSpecification))
            return Specification.where(attributeSpecification);

        return rootSpecification.or(attributeSpecification);
    }
}
