package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.CustomerTenant;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chandrashekar V
 */
public final class MdTenantBuilder {
    private int id;
    private String address;
    private String area;
    private String city;
    private Integer masterIdentifier;
    private String tenantIdentifier;
    private String tenantName;
    private String tenantShortCode;
    private MdOrganization mdOrganization;
    private Set<CustomerTenant> customerTenants = new HashSet<>();
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdTenantBuilder() {
    }

    public static MdTenantBuilder aMdTenant() {
        return new MdTenantBuilder();
    }

    public MdTenantBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdTenantBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public MdTenantBuilder withArea(String area) {
        this.area = area;
        return this;
    }

    public MdTenantBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public MdTenantBuilder withMasterIdentifier(Integer masterIdentifier) {
        this.masterIdentifier = masterIdentifier;
        return this;
    }

    public MdTenantBuilder withTenantIdentifier(String tenantIdentifier) {
        this.tenantIdentifier = tenantIdentifier;
        return this;
    }

    public MdTenantBuilder withTenantName(String tenantName) {
        this.tenantName = tenantName;
        return this;
    }

    public MdTenantBuilder withTenantShortCode(String tenantShortCode) {
        this.tenantShortCode = tenantShortCode;
        return this;
    }

    public MdTenantBuilder withMdOrganization(MdOrganization mdOrganization) {
        this.mdOrganization = mdOrganization;
        return this;
    }

    public MdTenantBuilder withCustomerTenants(Set<CustomerTenant> customerTenants) {
        this.customerTenants = customerTenants;
        return this;
    }

    public MdTenantBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdTenantBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdTenantBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdTenantBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdTenant build() {
        MdTenant mdTenant = new MdTenant();
        mdTenant.setId(id);
        mdTenant.setAddress(address);
        mdTenant.setArea(area);
        mdTenant.setCity(city);
        mdTenant.setMasterIdentifier(masterIdentifier);
        mdTenant.setTenantIdentifier(tenantIdentifier);
        mdTenant.setTenantName(tenantName);
        mdTenant.setTenantShortCode(tenantShortCode);
        mdTenant.setMdOrganization(mdOrganization);
        mdTenant.setCustomerTenants(customerTenants);
        mdTenant.setCreatedBy(createdBy);
        mdTenant.setCreatedDate(createdDate);
        mdTenant.setLastModifiedBy(lastModifiedBy);
        mdTenant.setLastModifiedDate(lastModifiedDate);
        return mdTenant;
    }
}
