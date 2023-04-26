package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceCategory;
import com.automate.vehicleservices.entity.MdServiceSchedulingConfig;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.LocalDateTime;

public final class MdServiceSchedulingConfigBuilder {
    private int id;
    private MdServiceType firstFreeServiceType;
    private MdServiceType pmcServiceType;
    private MdServiceCategory pmsServiceCategory;
    private MdServiceCategory pmcServiceCategory;
    private MdServiceCategory freeServiceCategory;
    private MdTenant mdTenant;
    private int pmsIntervalMonths;
    private int pmcIntervalMonths;
    private int pmsIntervalKm;
    private int pmcIntervalKm;
    private boolean remindersEnabled;
    private int firstFreeServiceDueDaysFromPurchaseDate;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdServiceSchedulingConfigBuilder() {
    }

    public static MdServiceSchedulingConfigBuilder aMdServiceSchedulingConfig() {
        return new MdServiceSchedulingConfigBuilder();
    }

    public MdServiceSchedulingConfigBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withFirstFreeServiceType(MdServiceType firstFreeServiceType) {
        this.firstFreeServiceType = firstFreeServiceType;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withPmcServiceType(MdServiceType pmcServiceType) {
        this.pmcServiceType = pmcServiceType;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withPmsServiceCategory(MdServiceCategory pmsServiceCategory) {
        this.pmsServiceCategory = pmsServiceCategory;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withPmcServiceCategory(MdServiceCategory pmcServiceCategory) {
        this.pmcServiceCategory = pmcServiceCategory;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withFreeServiceCategory(MdServiceCategory freeServiceCategory) {
        this.freeServiceCategory = freeServiceCategory;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withPmsIntervalMonths(int pmsIntervalMonths) {
        this.pmsIntervalMonths = pmsIntervalMonths;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withPmcIntervalMonths(int pmcIntervalMonths) {
        this.pmcIntervalMonths = pmcIntervalMonths;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withPmsIntervalKm(int pmsIntervalKm) {
        this.pmsIntervalKm = pmsIntervalKm;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withPmcIntervalKm(int pmcIntervalKm) {
        this.pmcIntervalKm = pmcIntervalKm;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withRemindersEnabled(boolean remindersEnabled) {
        this.remindersEnabled = remindersEnabled;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withFirstFreeServiceDueDaysFromPurchaseDate(
            int firstFreeServiceDueDaysFromPurchaseDate) {
        this.firstFreeServiceDueDaysFromPurchaseDate = firstFreeServiceDueDaysFromPurchaseDate;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdServiceSchedulingConfigBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdServiceSchedulingConfig build() {
        MdServiceSchedulingConfig mdServiceSchedulingConfig = new MdServiceSchedulingConfig();
        mdServiceSchedulingConfig.setId(id);
        mdServiceSchedulingConfig.setFirstFreeServiceType(firstFreeServiceType);
        mdServiceSchedulingConfig.setPmcServiceType(pmcServiceType);
        mdServiceSchedulingConfig.setPmsServiceCategory(pmsServiceCategory);
        mdServiceSchedulingConfig.setPmcServiceCategory(pmcServiceCategory);
        mdServiceSchedulingConfig.setFreeServiceCategory(freeServiceCategory);
        mdServiceSchedulingConfig.setMdTenant(mdTenant);
        mdServiceSchedulingConfig.setPmsIntervalMonths(pmsIntervalMonths);
        mdServiceSchedulingConfig.setPmcIntervalMonths(pmcIntervalMonths);
        mdServiceSchedulingConfig.setPmsIntervalKm(pmsIntervalKm);
        mdServiceSchedulingConfig.setPmcIntervalKm(pmcIntervalKm);
        mdServiceSchedulingConfig.setRemindersEnabled(remindersEnabled);
        mdServiceSchedulingConfig.setFirstFreeServiceDueDaysFromPurchaseDate(firstFreeServiceDueDaysFromPurchaseDate);
        mdServiceSchedulingConfig.setCreatedBy(createdBy);
        mdServiceSchedulingConfig.setCreatedDate(createdDate);
        mdServiceSchedulingConfig.setLastModifiedBy(lastModifiedBy);
        mdServiceSchedulingConfig.setLastModifiedDate(lastModifiedDate);
        return mdServiceSchedulingConfig;
    }
}
