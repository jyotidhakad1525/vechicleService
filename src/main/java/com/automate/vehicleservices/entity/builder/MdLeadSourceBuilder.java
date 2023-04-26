package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdLeadSource;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;

public final class MdLeadSourceBuilder {
    private final MdLeadSource mdLeadSource;

    private MdLeadSourceBuilder() {
        mdLeadSource = new MdLeadSource();
    }

    public static MdLeadSourceBuilder aMdLeadSource() {
        return new MdLeadSourceBuilder();
    }

    public MdLeadSourceBuilder withId(int id) {
        mdLeadSource.setId(id);
        return this;
    }

    public MdLeadSourceBuilder withType(String type) {
        mdLeadSource.setType(type);
        return this;
    }

    public MdLeadSourceBuilder withMdTenant(MdTenant mdTenant) {
        mdLeadSource.setMdTenant(mdTenant);
        return this;
    }

    public MdLeadSourceBuilder withActive(Boolean active) {
        mdLeadSource.setActive(active);
        return this;
    }

    public MdLeadSourceBuilder withParentId(MdLeadSource parentId) {
        mdLeadSource.setParentId(parentId);
        return this;
    }

    public MdLeadSourceBuilder withCreatedBy(String createdBy) {
        mdLeadSource.setCreatedBy(createdBy);
        return this;
    }

    public MdLeadSourceBuilder withCreatedDate(LocalDateTime createdDate) {
        mdLeadSource.setCreatedDate(createdDate);
        return this;
    }

    public MdLeadSourceBuilder withLastModifiedBy(String lastModifiedBy) {
        mdLeadSource.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public MdLeadSourceBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        mdLeadSource.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public MdLeadSource build() {
        return mdLeadSource;
    }
}
