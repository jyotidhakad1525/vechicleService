package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceItemRequest;
import com.automate.vehicleservices.api.md.MdServiceItemResponse;
import com.automate.vehicleservices.entity.MdServiceItem;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceItemBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdServiceItemRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
public class MdServiceItemService extends AbstractService implements MasterDataService {

    private final MdServiceItemRepository mdServiceItemRepository;

    @Autowired
    public MdServiceItemService(MdServiceItemRepository mdServiceItemRepository) {
        this.mdServiceItemRepository = mdServiceItemRepository;
    }

    public MdServiceItem findByIdAndTenant(int id, int tenantId) {
        return mdServiceItemRepository.findByIdAndMdTenant_IdAndIsActive(id, tenantId, true);
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdServiceItemRequest mdServiceItemRequest = (MdServiceItemRequest) mdRequest;
        final var mdTenantOptional = crudService.findById(tenantId, MdTenant.class);
        final var mdServiceItem = MdServiceItemBuilder.aMdServiceItem()
                .withMdTenant(mdTenantOptional.get())
                .withName(mdServiceItemRequest.getName())
                .withDescription(mdServiceItemRequest.getDescription())
                .withShortCode(mdServiceItemRequest.getShortCode())
                .build();
        final var save = crudService.save(mdServiceItem);
        return new MdServiceItemResponse(save);
    }

    @Transactional
    @Override
    public boolean delete(int id, int tenantId) {
        mdServiceItemRepository.deleteByIdAndMdTenant_Id(id, tenantId);
        return true;
    }

    @Transactional
    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var mdServiceItems = mdServiceItemRepository.findByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(mdServiceItems))
            return Collections.emptyList();

        return mdServiceItems.stream().map(MdServiceItemResponse::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MdResponse findById(int id, int tenantId) {
        final var mdServiceItem = getMdServiceItem(id, tenantId);
        if (Objects.isNull(mdServiceItem))
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Service Item not found for the given id");
        return new MdServiceItemResponse(mdServiceItem);
    }

    @Transactional
    public MdServiceItem getMdServiceItem(int id, int tenantId) {
        return mdServiceItemRepository.findByIdAndMdTenant_Id(id, tenantId);
    }

    /**
     * Fetch service items for the given id's.
     *
     * @param serviceTypeRequest
     * @return
     */
    Set<MdServiceItem> findAllById(List<Integer> serviceItems) {
        Set<MdServiceItem> mdServiceItems = new HashSet<>();
        try {
            if (CollectionUtils.isNotEmpty(serviceItems)) {
                final var serviceItemEntities = crudService.findAllById(serviceItems, MdServiceItem.class);
                serviceItemEntities.forEach(mdServiceItems::add);
            }
            return mdServiceItems;
        } catch (Exception ex) {
            throw new VehicleServicesException("Invalid items received.");
        }
    }

}
