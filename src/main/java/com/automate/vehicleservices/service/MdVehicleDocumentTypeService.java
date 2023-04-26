package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdVehicleDocumentTypeRequest;
import com.automate.vehicleservices.api.md.MdVehicleDocumentTypeResponse;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.MdVehicleDocumentType;
import com.automate.vehicleservices.entity.builder.MdVehicleDocumentTypeBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdVehicleDocumentTypeRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
public class MdVehicleDocumentTypeService extends AbstractService implements MasterDataService {

    private final MdVehicleDocumentTypeRepository mdVehicleDocumentTypeRepository;
    private final MdTenantService mdTenantService;

    @Autowired
    public MdVehicleDocumentTypeService(MdVehicleDocumentTypeRepository mdVehicleDocumentTypeRepository,
                                        MdTenantService mdTenantService) {
        this.mdVehicleDocumentTypeRepository = mdVehicleDocumentTypeRepository;
        this.mdTenantService = mdTenantService;
    }

    @Transactional
    public MdVehicleDocumentType findByTenantDocumentNameAndLabel(final String tenantIdentifier,
                                                                  @NotBlank final String documentName,
                                                                  final String label) {
        return mdVehicleDocumentTypeRepository.findByDocumentNameAndLabelAndMdTenant_TenantIdentifier(
                documentName, label, tenantIdentifier);

    }

    @Transactional
    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdVehicleDocumentTypeRequest mdVehicleDocumentTypeRequest = (MdVehicleDocumentTypeRequest) mdRequest;
        final var mdVehicleDocumentType = MdVehicleDocumentTypeBuilder.aMdVehicleDocumentType()
                .withDescription(mdVehicleDocumentTypeRequest.getDescription())
                .withDocumentName(mdVehicleDocumentTypeRequest.getDocumentName())
                .withLabel(mdVehicleDocumentTypeRequest.getLabel())
                .withMdTenant(crudService.findById(tenantId, MdTenant.class).get())
                .build();

        final var save = mdVehicleDocumentTypeRepository.save(mdVehicleDocumentType);
        return new MdVehicleDocumentTypeResponse(save);
    }

    @Transactional
    @Override
    public boolean delete(int id, int tenantId) {
        if (!mdVehicleDocumentTypeRepository.existsByIdAndMdTenant_Id(id, tenantId))
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Document Type not found to delete.");
        mdVehicleDocumentTypeRepository.deleteById(id);
        return true;
    }

    @Transactional
    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var vehicleDocumentTypes = mdVehicleDocumentTypeRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(vehicleDocumentTypes))
            return Collections.emptyList();

        return vehicleDocumentTypes.stream().map(MdVehicleDocumentTypeResponse::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MdResponse findById(int id, int tenantId) {
        final MdVehicleDocumentType mdVehicleDocumentType = getMdVehicleDocumentType(id, tenantId);
        return new MdVehicleDocumentTypeResponse(mdVehicleDocumentType);
    }

    @Transactional
    public MdVehicleDocumentType getMdVehicleDocumentType(int id, int tenantId) {
        return mdVehicleDocumentTypeRepository.findByIdAndMdTenant_Id(id, tenantId);
    }
}
