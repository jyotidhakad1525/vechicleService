package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceItem;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;

public final class MdServiceItemBuilder {
    private String description;
    private String name;
    private String shortCode;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdServiceItemBuilder() {
    }

    public static MdServiceItemBuilder aMdServiceItem() {
        return new MdServiceItemBuilder();
    }

    public MdServiceItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdServiceItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MdServiceItemBuilder withShortCode(String shortCode) {
        this.shortCode = shortCode;
        return this;
    }

    public MdServiceItemBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceItemBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdServiceItemBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdServiceItemBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdServiceItemBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdServiceItemBuilder but() {
        return aMdServiceItem().withDescription(description).withName(name).withShortCode(shortCode)
                .withMdTenant(mdTenant).withCreatedBy(createdBy).withCreatedDate(createdDate)
                .withLastModifiedBy(lastModifiedBy).withLastModifiedDate(lastModifiedDate);
    }

    public MdServiceItem build() {
        MdServiceItem mdServiceItem = new MdServiceItem();
        mdServiceItem.setDescription(description);
        mdServiceItem.setName(name);
        mdServiceItem.setShortCode(shortCode);
        mdServiceItem.setMdTenant(mdTenant);
        mdServiceItem.setCreatedBy(createdBy);
        mdServiceItem.setCreatedDate(createdDate);
        mdServiceItem.setLastModifiedBy(lastModifiedBy);
        mdServiceItem.setLastModifiedDate(lastModifiedDate);
        return mdServiceItem;
    }
}
