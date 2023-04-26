package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.repository.MdServiceTypeRepository;
import com.automate.vehicleservices.repository.dtoprojection.IServiceTypeDTO;
import com.automate.vehicleservices.repository.dtoprojection.MdServiceCategoryDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceItemByGroup;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Service
public class VehicleServiceTypeService {

    private final MdServiceTypeRepository serviceTypeRepository;

    private final MdServiceCategoryService serviceCategoryService;

    private final MdTenantService tenantService;

    public VehicleServiceTypeService(MdServiceTypeRepository serviceTypeRepository,
                                     MdServiceCategoryService serviceCategoryService, MdTenantService tenantService) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceCategoryService = serviceCategoryService;
        this.tenantService = tenantService;
    }

    public List<ServiceItemByGroup> fetchAdditionalServices(final int tenant, final ServiceGroup serviceGroup) {

        return serviceTypeRepository
                .fetchServiceItemsByCategoryGroup(tenant, serviceGroup);
    }

    public List<MdServiceCategoryDTO> activeServiceCategories(String tenant) {
        return serviceCategoryService.findByMdTenantAndIsActive(tenant, true);
    }

    public List<IServiceTypeDTO> activeServiceTypes(String tenant) {
        MdTenant mdTenant = tenantService.tenantByIdentifier(tenant);
        return serviceTypeRepository.findByMdTenantAndActive(mdTenant, true);
    }
}
