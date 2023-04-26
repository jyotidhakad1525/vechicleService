package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.ServiceLogicConfRequest;
import com.automate.vehicleservices.common.VehicleServiceConstants;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceLogicConfigurationDetails;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.ServiceLogicConfRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.ServiceLogicConfDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceLogicConfService extends AbstractService {

    private final ServiceLogicConfRepository serviceLogicConfRepository;

    private final MdTenantService tenantService;

    public ServiceLogicConfService(ServiceLogicConfRepository serviceLogicConfRepository, MdTenantService tenantService) {
        this.serviceLogicConfRepository = serviceLogicConfRepository;
        this.tenantService = tenantService;
    }


    @Transactional
    public ServiceLogicConfDTO addOrUpdateServiceLogicConf(ServiceLogicConfRequest request, MdOrganization mdOrganization) {
        Date date = Calendar.getInstance().getTime();
        ServiceLogicConfigurationDetails logicConfigurationDetails;
        if (Objects.nonNull(request.getId())) {
            //Update
            logicConfigurationDetails = getServiceLogicConfigurationDetails(request.getId(), mdOrganization.getId());
        } else {
            //Insert
            logicConfigurationDetails = new ServiceLogicConfigurationDetails();
            logicConfigurationDetails.setCreatedDatetime(date);
            logicConfigurationDetails.setOrganization(mdOrganization);
        }
        logicConfigurationDetails.setUpdatedDatetime(date);
        final var serviceCategory = crudService.findById(request.getServiceType(),
                MdServiceCategory.class);
        if (!serviceCategory.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "service type not found!!");
        logicConfigurationDetails.setServiceType(serviceCategory.get());

        final var serviceType = crudService.findById(request.getSubServiceType(),
                MdServiceType.class);
        if (!serviceType.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "sub service type not found!!");
        logicConfigurationDetails.setSubServiceType(serviceType.get());

        logicConfigurationDetails.setStatus(request.getStatus());
        logicConfigurationDetails.setEndDay(request.getEndDay());
        logicConfigurationDetails.setStartDay(request.getStartDay());
        logicConfigurationDetails.setKmEnd(request.getKmEnd());
        logicConfigurationDetails.setKmStart(request.getKmStart());

        final var result = serviceLogicConfRepository.save(logicConfigurationDetails);
        return new ServiceLogicConfDTO(result);
    }

    private ServiceLogicConfigurationDetails getServiceLogicConfigurationDetails(Integer id, Integer orgId) {
        Optional<ServiceLogicConfigurationDetails> logicConfigurationDetailsOptional = serviceLogicConfRepository.findByIdAndOrganizationId(id, orgId);
        if (!logicConfigurationDetailsOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Service Logic Configuration details not found with given id " + id);

        return logicConfigurationDetailsOptional.get();
    }

    @Transactional
    public PaginatedSearchResponse<ServiceLogicConfDTO> fetchAllServiceLogicConf(Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by(VehicleServiceConstants.CREATED_DATE_TIME).descending());
        final var pageResult = serviceLogicConfRepository.findAllByOrganizationId(orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new ServiceLogicConfDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public List<ServiceLogicConfigurationDetails> fetchAllServiceLogicConfBasedOnTenantId(String  tenantIdentifier) {
        MdTenant tenant = tenantService.tenantByIdentifier(tenantIdentifier);
        final var pageResult = serviceLogicConfRepository.findAllByOrganizationId(tenant.getMdOrganization().getId());
//        final var list = pageResult.stream().map(data -> new ServiceLogicConfDTO(data)).collect(Collectors.toList());
        return pageResult;
    }

    @Transactional
    public ServiceLogicConfDTO fetchServiceLogicConfBasedOnId(Integer id, int orgId) {
        return new ServiceLogicConfDTO(getServiceLogicConfigurationDetails(id, orgId));
    }

    @Transactional
    public void deleteServiceLogicConfBasedOnId(Integer id, int orgId) {
        ServiceLogicConfigurationDetails logicConfigurationDetails = getServiceLogicConfigurationDetails(id, orgId);
        serviceLogicConfRepository.delete(logicConfigurationDetails);
    }

    @Transactional
    public PaginatedSearchResponse<ServiceLogicConfDTO> search(ServiceLogicConfRequest request, Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by("created_datetime").descending());
        final var pageResult = serviceLogicConfRepository.search(request.getServiceType(), request.getStatus(), orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new ServiceLogicConfDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }
}
