package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.md.MdServiceCategoryRequest;
import com.automate.vehicleservices.api.md.MdServiceCategoryResponse;
import com.automate.vehicleservices.api.md.MdServiceTypeResponse;
import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.MdServiceCategoryBuilder;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.MdRequest;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MdServiceCategoryRepository;
import com.automate.vehicleservices.repository.dtoprojection.MdServiceCategoryDTO;
import com.automate.vehicleservices.service.dto.ServiceCategoryDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class MdServiceCategoryService extends AbstractService implements MasterDataService {

    private final MdServiceCategoryRepository mdServiceCategoryRepository;

    @Autowired
    public MdServiceCategoryService(MdServiceCategoryRepository mdServiceCategoryRepository) {
        this.mdServiceCategoryRepository = mdServiceCategoryRepository;
    }

    public List<MdServiceCategoryDTO> findByMdTenantAndIsActive(final String tenantIdentifier, boolean isActive) {

        MdTenant tenant = tenantService.tenantByIdentifier(tenantIdentifier);
        if (Objects.isNull(tenant))
            throw new VehicleServicesException("Service Center doesn't exist.");

        return mdServiceCategoryRepository.findByMdTenantAndActive(tenant, isActive);
    }

    public ServiceCategoryDTO findByMdTenantAndCategoryName(final String tenant,
                                                            final String categoryName) {

        MdServiceCategory mdServiceCategory = mdServiceCategoryRepository
                .findFirstByMdTenant_TenantIdentifierAndCategoryName(tenant, categoryName);

        return new ServiceCategoryDTO(mdServiceCategory);
    }

    @Override
    public MdResponse save(MdRequest mdRequest, int tenantId) {
        MdServiceCategoryRequest mdServiceCategoryRequest = (MdServiceCategoryRequest) mdRequest;
        final var mdTenantOptional = crudService.findById(tenantId, MdTenant.class);
        final var mdServiceCategory = MdServiceCategoryBuilder.aMdServiceCategory()
                .withCategoryName(mdServiceCategoryRequest.getCategoryName())
                .withActive(true)
                .withServiceGroup(mdServiceCategoryRequest.getServiceGroup())
                .withMdTenant(mdTenantOptional.get())
                .build();
        final var save = crudService.save(mdServiceCategory);
        return new MdServiceCategoryResponse(save);
    }

    @Override
    public boolean delete(int id, int tenantId) {
        final var count = mdServiceCategoryRepository.deactivate(id, tenantId);
        if (count == 0)
            throw new VehicleServicesException("Updated failed. Given service category and tenant combination do not " +
                    "exists.");

        return true;
    }

    @Override
    public List<? extends MdResponse> all(int tenantId) {
        final var serviceCategoryList = mdServiceCategoryRepository.findAllByMdTenant_Id(tenantId);

        if (CollectionUtils.isEmpty(serviceCategoryList))
            return Collections.emptyList();

        return serviceCategoryList.stream().map(MdServiceCategoryResponse::new).collect(Collectors.toList());
    }

    @Override
    public MdResponse findById(int id, int tenantId) {
        final var mdServiceCategory = getServiceCategoryById(id, tenantId);
        return new MdServiceCategoryResponse(mdServiceCategory);
    }

    @Transactional
    private MdServiceCategory getServiceCategoryById(int id, int tenantId) {
        return mdServiceCategoryRepository.findByIdAndMdTenant_Id(id, tenantId);
    }

    @Transactional
    public List<MdServiceTypeResponse> findServiceTypesByCategory(int category, int tenantId) {
        final var serviceCategoryById = getServiceCategoryById(category, tenantId);
        if (Objects.isNull(serviceCategoryById))
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Category not found");
        final var mdServiceTypes = serviceCategoryById.getMdServiceTypes();
        if (CollectionUtils.isEmpty(mdServiceTypes))
            return Collections.emptyList();

        return mdServiceTypes.stream().map(MdServiceTypeResponse::new).collect(Collectors.toList());
    }
}
