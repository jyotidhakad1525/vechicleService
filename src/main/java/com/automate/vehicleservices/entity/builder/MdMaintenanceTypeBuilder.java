package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdMaintenanceType;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;

public final class MdMaintenanceTypeBuilder {
    private int id;
    private String description;
    private String shortCode;
    private String type;
    private Boolean active;
    private MdTenant mdTenant;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdMaintenanceTypeBuilder() {
    }

    public static MdMaintenanceTypeBuilder aMdMaintenanceType() {
        return new MdMaintenanceTypeBuilder();
    }

    public MdMaintenanceTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdMaintenanceTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdMaintenanceTypeBuilder withShortCode(String shortCode) {
        this.shortCode = shortCode;
        return this;
    }

    public MdMaintenanceTypeBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public MdMaintenanceTypeBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public MdMaintenanceTypeBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdMaintenanceTypeBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdMaintenanceTypeBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdMaintenanceTypeBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdMaintenanceTypeBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdMaintenanceType build() {
        MdMaintenanceType mdMaintenanceType = new MdMaintenanceType();
        mdMaintenanceType.setId(id);
        mdMaintenanceType.setDescription(description);
        mdMaintenanceType.setShortCode(shortCode);
        mdMaintenanceType.setType(type);
        mdMaintenanceType.setActive(active);
        mdMaintenanceType.setMdTenant(mdTenant);
        mdMaintenanceType.setCreatedBy(createdBy);
        mdMaintenanceType.setCreatedDate(createdDate);
        mdMaintenanceType.setLastModifiedBy(lastModifiedBy);
        mdMaintenanceType.setLastModifiedDate(lastModifiedDate);
        return mdMaintenanceType;
    }
}
