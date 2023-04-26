package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.ServiceLogicReminderRequest;
import com.automate.vehicleservices.common.VehicleServiceConstants;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.ServiceLogicReminderDetails;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.ServiceLogicReminderRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.ServiceLogicReminderDTO;
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
public class ServiceLogicReminderService extends AbstractService {

    private final ServiceLogicReminderRepository logicReminderRepository;

    public ServiceLogicReminderService(ServiceLogicReminderRepository logicReminderRepository) {
        this.logicReminderRepository = logicReminderRepository;
    }

    @Transactional
    public ServiceLogicReminderDTO addOrUpdateServiceLogicReminder(ServiceLogicReminderRequest request, MdOrganization mdOrganization) {
        Date date = Calendar.getInstance().getTime();
        ServiceLogicReminderDetails reminderDetails;
        if (Objects.nonNull(request.getId())) {
            // Update
            reminderDetails = getServiceLogicReminderDetails(request.getId(), mdOrganization.getId());
        } else {
            // Insert
            reminderDetails = new ServiceLogicReminderDetails();
            reminderDetails.setCreatedDatetime(date);
            reminderDetails.setOrganization(mdOrganization);
        }

        final var serviceCategory = crudService.findById(request.getServiceType(),
                MdServiceCategory.class);
        if (!serviceCategory.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "service type not found!!");
        reminderDetails.setServiceType(serviceCategory.get());

        final var serviceType = crudService.findById(request.getSubServiceType(),
                MdServiceType.class);
        if (!serviceType.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "sub service type not found!!");
        reminderDetails.setSubServiceType(serviceType.get());

        reminderDetails.setReminderDays(request.getReminderDays());
        reminderDetails.setStatus(request.getStatus());
        reminderDetails.setUpdatedDatetime(date);
        final var result = logicReminderRepository.save(reminderDetails);
        return new ServiceLogicReminderDTO(result);
    }

    private ServiceLogicReminderDetails getServiceLogicReminderDetails(Integer id, Integer orgId) {
        Optional<ServiceLogicReminderDetails> detailsOptional = logicReminderRepository.findByIdAndOrganizationId(id, orgId);
        if (!detailsOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Service Logic Reminder details not found with given id " + id);
        return detailsOptional.get();
    }

    @Transactional
    public PaginatedSearchResponse<ServiceLogicReminderDTO> fetchAllServiceLogicReminder(Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by(VehicleServiceConstants.CREATED_DATE_TIME).descending());
        final var pageResult = logicReminderRepository.findAllByOrganizationId(orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new ServiceLogicReminderDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public List<ServiceLogicReminderDetails> fetchAllServiceLogicReminderList(int orgId) {
        final var result = logicReminderRepository.findAllByOrganizationId(orgId);
        return result;
    }

    @Transactional
    public ServiceLogicReminderDetails fetchServiceLogicReminderDetailsBasedOnServiceTypeId(Integer subServiceTypeId) {
        final var result = logicReminderRepository.findBySubServiceTypeId(subServiceTypeId);
        if (!result.isPresent()) {
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "reminder details not found!!");
        }
        return result.get();
    }

    @Transactional
    public ServiceLogicReminderDTO fetchServiceLogicReminderBasedOnId(Integer id, int orgId) {
        ServiceLogicReminderDetails reminderDetails = getServiceLogicReminderDetails(id, orgId);
        return new ServiceLogicReminderDTO(reminderDetails);
    }

    @Transactional
    public void deleteServiceLogicReminderBasedOnId(Integer id, int orgId) {
        ServiceLogicReminderDetails reminderDetails = getServiceLogicReminderDetails(id, orgId);
        logicReminderRepository.delete(reminderDetails);
    }

    @Transactional
    public PaginatedSearchResponse<ServiceLogicReminderDTO> search(ServiceLogicReminderRequest request, Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by("created_datetime").descending());
        final var pageResult = logicReminderRepository.search(request.getServiceType(), request.getSubServiceType(), request.getStatus(), request.getReminderDays(), orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new ServiceLogicReminderDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }
}
