package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdServiceTypeCriteria;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;

public final class MdServiceTypeCriteriaBuilder {
    private String description;
    private int durationDaysTo;
    private int durationDaysFrom;
    private int mileageFrom;
    private int mileageTo;
    private MdTenant mdTenant;
    private MdServiceType mdServiceType;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdServiceTypeCriteriaBuilder() {
    }

    public static MdServiceTypeCriteriaBuilder aMdServiceTypeCriteria() {
        return new MdServiceTypeCriteriaBuilder();
    }

    public MdServiceTypeCriteriaBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withDurationDaysTo(int durationDaysTo) {
        this.durationDaysTo = durationDaysTo;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withDurationDaysFrom(int durationDaysFrom) {
        this.durationDaysFrom = durationDaysFrom;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withMileageFrom(int mileageFrom) {
        this.mileageFrom = mileageFrom;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withMileageTo(int mileageTo) {
        this.mileageTo = mileageTo;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }


    public MdServiceTypeCriteriaBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdServiceTypeCriteriaBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdServiceTypeCriteria build() {
        MdServiceTypeCriteria mdServiceTypeCriteria = new MdServiceTypeCriteria();
        mdServiceTypeCriteria.setDescription(description);
        mdServiceTypeCriteria.setDurationDaysTo(durationDaysTo);
        mdServiceTypeCriteria.setDurationDaysFrom(durationDaysFrom);
        mdServiceTypeCriteria.setMileageFrom(mileageFrom);
        mdServiceTypeCriteria.setMileageTo(mileageTo);
        mdServiceTypeCriteria.setMdTenant(mdTenant);
        mdServiceTypeCriteria.setMdServiceType(mdServiceType);
        mdServiceTypeCriteria.setCreatedBy(createdBy);
        mdServiceTypeCriteria.setCreatedDate(createdDate);
        mdServiceTypeCriteria.setLastModifiedBy(lastModifiedBy);
        mdServiceTypeCriteria.setLastModifiedDate(lastModifiedDate);
        return mdServiceTypeCriteria;
    }
}
