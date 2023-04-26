package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
public final class MdServiceTypeBuilder {
    private int id;
    private String description;
    private boolean active;
    private String serviceCode;
    private String serviceName;
    private MdTenant mdTenant;
    private MdServiceCategory mdServiceCategory;
    private Set<MdServiceItem> serviceItems = new HashSet<>();
    private List<MdServiceTypeCriteria> mdServiceTypeCriteria = new ArrayList<>();
    private List<MdServiceTypeRule> mdServiceTypeRules = new ArrayList<>();
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdServiceTypeBuilder() {
    }

    public static MdServiceTypeBuilder aMdServiceType() {
        return new MdServiceTypeBuilder();
    }

    public MdServiceTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdServiceTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdServiceTypeBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public MdServiceTypeBuilder withServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
        return this;
    }

    public MdServiceTypeBuilder withServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public MdServiceTypeBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceTypeBuilder withMdServiceCategory(MdServiceCategory mdServiceCategory) {
        this.mdServiceCategory = mdServiceCategory;
        return this;
    }

    public MdServiceTypeBuilder withServiceItems(Set<MdServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
        return this;
    }

    public MdServiceTypeBuilder withMdServiceTypeCriteria(List<MdServiceTypeCriteria> mdServiceTypeCriteria) {
        this.mdServiceTypeCriteria = mdServiceTypeCriteria;
        return this;
    }

    public MdServiceTypeBuilder withMdServiceTypeRules(List<MdServiceTypeRule> mdServiceTypeRules) {
        this.mdServiceTypeRules = mdServiceTypeRules;
        return this;
    }

    public MdServiceTypeBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdServiceTypeBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdServiceTypeBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdServiceTypeBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdServiceType build() {
        MdServiceType mdServiceType = new MdServiceType();
        mdServiceType.setId(id);
        mdServiceType.setDescription(description);
        mdServiceType.setActive(active);
        mdServiceType.setServiceCode(serviceCode);
        mdServiceType.setServiceName(serviceName);
        mdServiceType.setMdTenant(mdTenant);
        mdServiceType.setMdServiceCategory(mdServiceCategory);
        mdServiceType.addServiceItems(serviceItems);
        mdServiceType.setMdServiceTypeCriteria(mdServiceTypeCriteria);
        mdServiceType.setMdServiceTypeRules(mdServiceTypeRules);
        mdServiceType.setCreatedBy(createdBy);
        mdServiceType.setCreatedDate(createdDate);
        mdServiceType.setLastModifiedBy(lastModifiedBy);
        mdServiceType.setLastModifiedDate(lastModifiedDate);
        return mdServiceType;
    }
}
