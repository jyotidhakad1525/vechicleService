package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class MdOrganizationBuilder {
    private int id;
    private Integer masterIdentifier;
    private String description;
    private byte isActive;
    private String orgIdentifier;
    private String orgName;
    private String orgShortCode;
    private List<MdTenant> mdTenants = new ArrayList<>();
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdOrganizationBuilder() {
    }

    public static MdOrganizationBuilder aMdOrganization() {
        return new MdOrganizationBuilder();
    }

    public MdOrganizationBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdOrganizationBuilder withMasterIdentifier(Integer masterIdentifier) {
        this.masterIdentifier = masterIdentifier;
        return this;
    }

    public MdOrganizationBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdOrganizationBuilder withIsActive(byte isActive) {
        this.isActive = isActive;
        return this;
    }

    public MdOrganizationBuilder withOrgIdentifier(String orgIdentifier) {
        this.orgIdentifier = orgIdentifier;
        return this;
    }

    public MdOrganizationBuilder withOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public MdOrganizationBuilder withOrgShortCode(String orgShortCode) {
        this.orgShortCode = orgShortCode;
        return this;
    }

    public MdOrganizationBuilder withMdTenants(List<MdTenant> mdTenants) {
        this.mdTenants = mdTenants;
        return this;
    }

    public MdOrganizationBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdOrganizationBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdOrganizationBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdOrganizationBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdOrganization build() {
        MdOrganization mdOrganization = new MdOrganization();
        mdOrganization.setId(id);
        mdOrganization.setMasterIdentifier(masterIdentifier);
        mdOrganization.setDescription(description);
        mdOrganization.setIsActive(isActive);
        mdOrganization.setOrgIdentifier(orgIdentifier);
        mdOrganization.setOrgName(orgName);
        mdOrganization.setOrgShortCode(orgShortCode);
        mdOrganization.setMdTenants(mdTenants);
        mdOrganization.setCreatedBy(createdBy);
        mdOrganization.setCreatedDate(createdDate);
        mdOrganization.setLastModifiedBy(lastModifiedBy);
        mdOrganization.setLastModifiedDate(lastModifiedDate);
        return mdOrganization;
    }
}
