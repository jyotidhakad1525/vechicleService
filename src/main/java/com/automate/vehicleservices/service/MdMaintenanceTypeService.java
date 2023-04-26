package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdMaintenanceTypeRequest;
import com.automate.vehicleservices.api.md.MdMaintenanceTypeResponse;
import com.automate.vehicleservices.entity.MdMaintenanceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdMaintenanceTypeBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.repository.MdMaintenanceTypeRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
public class MdMaintenanceTypeService extends AbstractService implements MasterDataService {

    private final MdMaintenanceTypeRepository mdMaintenanceTypeRepository;

    @Autowired
    public MdMaintenanceTypeService(MdMaintenanceTypeRepository mdMaintenanceTypeRepository) {
        this.mdMaintenanceTypeRepository = mdMaintenanceTypeRepository;
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdMaintenanceTypeRequest mdMaintenanceTypeRequest = (MdMaintenanceTypeRequest) mdRequest;
        final var mdTenantOptional = crudService.findById(tenantId, MdTenant.class);
        final var mdServiceItem = MdMaintenanceTypeBuilder.aMdMaintenanceType()
                .withMdTenant(mdTenantOptional.get())
                .withType(mdMaintenanceTypeRequest.getType())
                .withDescription(mdMaintenanceTypeRequest.getDescription())
                .withShortCode(mdMaintenanceTypeRequest.getShortCode())
                .withActive(true)
                .build();
        final var save = crudService.save(mdServiceItem);
        return new MdMaintenanceTypeResponse(save);
    }

    @Override
    public boolean delete(int id, int tenantId) {
        mdMaintenanceTypeRepository.deactivate(id, tenantId);
        return true;
    }

    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var mdMaintenanceTypes = mdMaintenanceTypeRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(mdMaintenanceTypes))
            return Collections.emptyList();

        return mdMaintenanceTypes.stream().map(MdMaintenanceTypeResponse::new).collect(Collectors.toList());
    }

    @Override
    public MdResponse findById(int id, int tenantId) {
        final MdMaintenanceType mdMaintenanceType = getMdMaintenanceType(id, tenantId);
        return new MdMaintenanceTypeResponse(mdMaintenanceType);
    }

    @Transactional
    public MdMaintenanceType getMdMaintenanceType(int id, int tenantId) {
        return mdMaintenanceTypeRepository.findByIdAndMdTenant_Id(id, tenantId);
    }

}
