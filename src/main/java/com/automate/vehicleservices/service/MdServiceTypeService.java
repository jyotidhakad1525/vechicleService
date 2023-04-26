package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceItemMappingRequest;
import com.automate.vehicleservices.api.md.MdServiceTypeRequest;
import com.automate.vehicleservices.api.md.MdServiceTypeResponse;
import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceTypeBuilder;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdServiceTypeRepository;
import com.automate.vehicleservices.repository.dtoprojection.ServiceTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
@Slf4j
public class MdServiceTypeService extends AbstractService implements MasterDataService {

    private final MdServiceTypeRepository mdServiceTypeRepository;
    private final MdServiceItemService mdServiceItemService;

    @Autowired
    public MdServiceTypeService(MdServiceTypeRepository mdServiceTypeRepository,
                                MdServiceItemService mdServiceItemService) {
        this.mdServiceTypeRepository = mdServiceTypeRepository;
        this.mdServiceItemService = mdServiceItemService;
    }

    @Transactional
    public ServiceTypeDTO findById(final int id) {
        return mdServiceTypeRepository.findById(id).map(ServiceTypeDTO::new).orElse(null);
    }

    @Transactional
    public MdServiceType findNextServiceInSequence(int previousPMSServiceTypeId, final String tenant) {
        log.info(String.format("Trying to fetch service by id: %d and tenant %s", previousPMSServiceTypeId, tenant));
        ServiceTypeDTO byId = findById(previousPMSServiceTypeId);
        log.info(String.format("Service type for id : %d is, %s", previousPMSServiceTypeId,
                (byId != null ? byId.toString() : "NULL")));
        if (Objects.nonNull(byId) && byId.getSequence() > 0) {

            int nextServiceSequence = byId.getSequence() + 1; // Next Sequence
            MdServiceType mdServiceType =
                    mdServiceTypeRepository
                            .findFirstByMdTenant_TenantIdentifierAndActiveTrueAndServiceSequenceEquals(tenant,
                                    nextServiceSequence);

            if (Objects.nonNull(mdServiceType)) {
                return mdServiceType;
            }
            log.warn(String.format("No Next sequence exists for service type %d, tenant %s", previousPMSServiceTypeId,
                    tenant));
        }
        log.warn(String.format("Invalid sequence. For service type ID: %d, tenant: %s", previousPMSServiceTypeId,
                tenant));
        return null;
    }

    @Transactional
    public MdServiceType findByServiceName(String periodicMaintenanceService, String tenantId) {
        return mdServiceTypeRepository
                .findFirstByMdTenant_TenantIdentifierAndActiveTrueAndServiceName(tenantId, periodicMaintenanceService);
    }

    @Transactional
    public MdServiceTypeResponse save(MdRequest mdRequest, int tenant) {
        MdServiceTypeRequest serviceTypeRequest = (MdServiceTypeRequest) mdRequest;
        final var serviceCategory = crudService.findById(serviceTypeRequest.getServiceCategoryId(),
                MdServiceCategory.class);
        final var sc = serviceCategory.get();
        validateBeforeCreate(serviceTypeRequest, sc);
        final var mdServiceItems = mdServiceItemService.findAllById(serviceTypeRequest.getServiceItems());

        final var mdTenantOptional = crudService.findById(tenant, MdTenant.class);
        final var mdServiceType = MdServiceTypeBuilder.aMdServiceType()
                .withServiceCode(serviceTypeRequest.getServiceCode())
                .withServiceName(serviceTypeRequest.getServiceName())
                .withActive(true)
                .withMdServiceCategory(sc)
                .withDescription(serviceTypeRequest.getDescription())
                .withMdTenant(mdTenantOptional.get())
                .withServiceItems(mdServiceItems)
                .build();
        final var save = crudService.save(mdServiceType);
        return new MdServiceTypeResponse(save);
    }


    private void validateBeforeCreate(MdServiceTypeRequest serviceTypeRequest, MdServiceCategory serviceCategory) {

        if (Objects.isNull(serviceCategory))
            throw new VehicleServicesException("Service Category must be valid.");

        if (serviceCategory.getServiceGroup() == ServiceGroup.REGULAR_MAINTENANCE)
            if (serviceTypeRequest.getPmsSequence() == 0)
                throw new VehicleServicesException("For regular maintenance, a valid service sequence must be chosen.");
            else if (serviceCategory.getMdServiceTypes().stream().anyMatch(st -> st.getServiceSequence() == serviceTypeRequest.getPmsSequence()))
                throw new VehicleServicesException("Duplicate sequence has been chosen. Chose a unique sequence");

    }

    @Transactional
    public boolean delete(int id, int tenantId) {
        final var count = mdServiceTypeRepository.deactivate(id, tenantId);
        return true;
    }

    @Transactional
    public boolean activate(int id, int tenantId) {
        final var count = mdServiceTypeRepository.activate(id, tenantId);
        if (count == 0)
            throw new VehicleServicesException("Updated failed. Given service type and tenant combination do not " +
                    "exists.");

        return true;
    }


    @Transactional
    public List<MdServiceTypeResponse> all(int tenantId) {
        final var serviceTypeList = mdServiceTypeRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(serviceTypeList))
            return Collections.emptyList();

        return serviceTypeList.stream().map(MdServiceTypeResponse::new).collect(Collectors.toList());
    }


    @Transactional
    public MdServiceTypeResponse findById(int id, int tenantId) {
        final MdServiceType mdServiceType = getMdServiceType(id, tenantId);
        return new MdServiceTypeResponse(mdServiceType);
    }

    @Transactional
    public MdServiceType getMdServiceType(int id, int tenantId) {
        return mdServiceTypeRepository.findByIdAndMdTenant_IdAndActiveTrue(id, tenantId);
    }

    @Transactional
    public MdServiceTypeResponse mapServiceItemToServiceType(int serviceTypeId,
                                                             MdServiceItemMappingRequest mdServiceItemMappingRequest,
                                                             int tenantId) {
        final MdServiceType mdServiceType = getMdServiceTypeOrThrow(serviceTypeId, tenantId);

        final var mdServiceItems = mdServiceItemService.findAllById(mdServiceItemMappingRequest.getServiceItems());
        mdServiceType.addServiceItems(mdServiceItems);
        final var save = mdServiceTypeRepository.save(mdServiceType);
        return new MdServiceTypeResponse(save);
    }

    private MdServiceType getMdServiceTypeOrThrow(int serviceTypeId, int tenantId) {
        final var mdServiceType = getMdServiceType(serviceTypeId, tenantId);
        if (Objects.isNull(mdServiceType))
            throw new VehicleServicesException("Mapping failed. Invalid Service type. ");
        return mdServiceType;
    }

    @Transactional
    public MdServiceTypeResponse deleteServiceItemToServiceTypeMapping(int serviceTypeId,
                                                                       MdServiceItemMappingRequest mdServiceItemMappingRequest, int tenantId) {
        final MdServiceType mdServiceType = getMdServiceTypeOrThrow(serviceTypeId, tenantId);

        final var mdServiceItems = mdServiceItemService.findAllById(mdServiceItemMappingRequest.getServiceItems());
        final var b = mdServiceType.removeServiceItems(mdServiceItems);
        if (!b)
            throw new VehicleServicesException("Removal failed");
        final var save = mdServiceTypeRepository.save(mdServiceType);
        return new MdServiceTypeResponse(save);
    }
}
