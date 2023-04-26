package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.enums.ServiceGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class MdServiceCategoryBuilder {
    private int id;
    private String categoryName;
    private boolean active;
    private ServiceGroup serviceGroup;
    private List<MdServiceType> mdServiceTypes = new ArrayList<>();
    private MdTenant mdTenant;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdServiceCategoryBuilder() {
    }

    public static MdServiceCategoryBuilder aMdServiceCategory() {
        return new MdServiceCategoryBuilder();
    }

    public MdServiceCategoryBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdServiceCategoryBuilder withCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public MdServiceCategoryBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public MdServiceCategoryBuilder withServiceGroup(ServiceGroup serviceGroup) {
        this.serviceGroup = serviceGroup;
        return this;
    }

    public MdServiceCategoryBuilder withMdServiceTypes(List<MdServiceType> mdServiceTypes) {
        this.mdServiceTypes = mdServiceTypes;
        return this;
    }

    public MdServiceCategoryBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceCategoryBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdServiceCategoryBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdServiceCategoryBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdServiceCategoryBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdServiceCategory build() {
        MdServiceCategory mdServiceCategory = new MdServiceCategory();
        mdServiceCategory.setId(id);
        mdServiceCategory.setCategoryName(categoryName);
        mdServiceCategory.setActive(active);
        mdServiceCategory.setServiceGroup(serviceGroup);
        mdServiceCategory.setMdServiceTypes(mdServiceTypes);
        mdServiceCategory.setMdTenant(mdTenant);
        mdServiceCategory.setCreatedBy(createdBy);
        mdServiceCategory.setCreatedDate(createdDate);
        mdServiceCategory.setLastModifiedBy(lastModifiedBy);
        mdServiceCategory.setLastModifiedDate(lastModifiedDate);
        return mdServiceCategory;
    }
}
