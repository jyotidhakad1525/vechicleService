package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.framework.exception.ResourceNotFoundException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.MdOrganizationRepository;
import com.automate.vehicleservices.repository.MdTenantRepository;
import com.automate.vehicleservices.service.dto.TenantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
public class MdTenantService {

    private final MdTenantRepository tenantRepository;

    private final MdOrganizationRepository organizationRepository;

    private final CrudService crudService;

    @Autowired
    public MdTenantService(MdTenantRepository tenantRepository, CrudService crudService,
                           MdOrganizationRepository organizationRepository) {
        this.tenantRepository = tenantRepository;
        this.crudService = crudService;
        this.organizationRepository = organizationRepository;
    }

    /**
     * Fetches list of distinct cities where all tenants are located.
     *
     * @param orgIdentifier
     * @return
     */
    public List<String> listOfCitiesByTheOrganization(final String orgIdentifier) {
        ifOrgDoesntExistsThrowException(orgIdentifier);
        return tenantRepository.findDistinctCities(orgIdentifier);
    }

    public void ifOrgDoesntExistsThrowException(final String orgIdentifier) {
        MdOrganization organization = organizationRepository.findByOrgIdentifier(orgIdentifier);
        if (Objects.isNull(organization))
            throw new ResourceNotFoundException(String.format("Organization not found for identifier %s",
                    orgIdentifier));
    }

    /**
     * Fetches list of distinct cities where all tenants are located.
     *
     * @param orgIdentifier
     * @return
     */
    public List<TenantDTO> listOfTenantsByTheCityAndOrganization(final String city, final String orgIdentifier) {
        ifOrgDoesntExistsThrowException(orgIdentifier);
        List<MdTenant> tenants = tenantRepository
                .findByCityAndIsActiveTrueAndMdOrganization_OrgIdentifier(city, orgIdentifier);

        return tenants.stream().filter(Objects::nonNull)
                .map(mdTenant -> new TenantDTO.TenantDTOBuilder().withIdentifier(mdTenant.getTenantIdentifier())
                        .withId(mdTenant.getId())
                        .withName(mdTenant.getTenantName()).withOrganization(mdTenant.getMdOrganization().getOrgName())
                        .build())
                .collect(Collectors.toList());
    }

    public MdTenant tenantByIdentifier(final String tenantIdentifier) {
        return tenantRepository.findByTenantIdentifier(tenantIdentifier);
    }

    public MdTenant findByMasterIdentifier(int tenant) {
        return tenantRepository.findTenantIdentifierByMasterIdentifier(tenant);
    }
}
