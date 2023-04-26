package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.ServiceFollowUpReasonRequest;
import com.automate.vehicleservices.common.VehicleServiceConstants;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.ServiceFollowUpReasonDetails;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.ServiceFollowUpReasonRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.ServiceFollowUpReasonDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceFollowUpReasonService extends AbstractService {

    private final ServiceFollowUpReasonRepository reasonRepository;

    public ServiceFollowUpReasonService(ServiceFollowUpReasonRepository reasonRepository) {
        this.reasonRepository = reasonRepository;
    }


    @Transactional
    public ServiceFollowUpReasonDTO addOrUpdateServiceFollowUpReason(ServiceFollowUpReasonRequest request, MdOrganization mdOrganization) {
        Date date = Calendar.getInstance().getTime();
        ServiceFollowUpReasonDetails followUpReasonDetails;
        if (Objects.nonNull(request.getId())) {
            // Update
            followUpReasonDetails = getServiceFollowUpReasonDetails(request.getId(), mdOrganization.getId());
        } else {
            // Insert
            followUpReasonDetails = new ServiceFollowUpReasonDetails();
            followUpReasonDetails.setCreatedDatetime(date);
            followUpReasonDetails.setOrganization(mdOrganization);
        }

        final var serviceCategory = crudService.findById(request.getServiceType(),
                MdServiceCategory.class);
        if (!serviceCategory.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "service type not found!!");
        followUpReasonDetails.setServiceType(serviceCategory.get());

        final var serviceType = crudService.findById(request.getSubServiceType(),
                MdServiceType.class);
        if (!serviceType.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "sub service type not found!!");
        followUpReasonDetails.setSubServiceType(serviceType.get());

        followUpReasonDetails.setReason(request.getReason());
        followUpReasonDetails.setStatus(request.getStatus());
        followUpReasonDetails.setUpdatedDatetime(date);
        final var result = reasonRepository.save(followUpReasonDetails);
        return new ServiceFollowUpReasonDTO(result);
    }

    private ServiceFollowUpReasonDetails getServiceFollowUpReasonDetails(Integer id, Integer orgId) {
        Optional<ServiceFollowUpReasonDetails> detailsOptional = reasonRepository.findByIdAndOrganizationId(id, orgId);
        if (!detailsOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Service Follow-Up Reason details not found with given id " + id);
        return detailsOptional.get();
    }

    @Transactional
    public PaginatedSearchResponse<ServiceFollowUpReasonDTO> fetchAllServiceFollowUpReason(Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by(VehicleServiceConstants.CREATED_DATE_TIME).descending());
        final var pageResult = reasonRepository.findAllByOrganizationId(orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new ServiceFollowUpReasonDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public ServiceFollowUpReasonDTO fetchServiceFollowUpReasonBasedOnId(Integer id, int orgId) {
        ServiceFollowUpReasonDetails followUpReasonDetails = getServiceFollowUpReasonDetails(id, orgId);
        return new ServiceFollowUpReasonDTO(followUpReasonDetails);
    }

    @Transactional
    public void deleteServiceFollowUpReasonBasedOnId(Integer id, int orgId) {
        ServiceFollowUpReasonDetails followUpReasonDetails = getServiceFollowUpReasonDetails(id, orgId);
        reasonRepository.delete(followUpReasonDetails);
    }

    @Transactional
    public PaginatedSearchResponse<ServiceFollowUpReasonDTO> search(ServiceFollowUpReasonRequest request, Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by("created_datetime").descending());
        final var pageResult = reasonRepository.search(request.getServiceType(), request.getSubServiceType(), request.getStatus(), request.getReason(), orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new ServiceFollowUpReasonDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }
}
