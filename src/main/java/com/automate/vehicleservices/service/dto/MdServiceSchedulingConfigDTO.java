package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.MdServiceSchedulingConfig;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MdServiceSchedulingConfigDTO {
    private int id;
    private int firstFreeServiceType;
    private String firstFreeServiceName;
    private int pmsServiceCategory;
    private String pmsServiceCategoryName;
    private int pmcServiceCategory;
    private String pmcServiceCategoryName;
    private int pmcServiceType;
    private int freeServiceCategory;
    private String freeServiceCategoryName;
    private int tenantId;
    private String tenantIdentifier;
    private int pmsIntervalMonths; // 12 months
    private int pmcIntervalMonths; // 6months
    private int pmsIntervalKm; //  10000
    private int pmcIntervalKm; // 5000
    private int firstFreeServiceDueDaysFromPurchaseDate;
    private boolean remindersEnabled;

    public MdServiceSchedulingConfigDTO(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        this.id = mdServiceSchedulingConfig.getId();
        this.firstFreeServiceType = mdServiceSchedulingConfig.getFirstFreeServiceType().getId();
        this.pmsServiceCategory = mdServiceSchedulingConfig.getId();
        this.pmsServiceCategoryName = mdServiceSchedulingConfig.getPmsServiceCategory().getCategoryName();
        this.pmcServiceCategory = mdServiceSchedulingConfig.getPmcServiceCategory().getId();
        this.pmcServiceCategoryName = mdServiceSchedulingConfig.getPmcServiceCategory().getCategoryName();
        this.freeServiceCategory = mdServiceSchedulingConfig.getFreeServiceCategory().getId();
        this.freeServiceCategoryName = mdServiceSchedulingConfig.getFreeServiceCategory().getCategoryName();
        this.tenantId = mdServiceSchedulingConfig.getMdTenant().getId();
        this.tenantIdentifier = mdServiceSchedulingConfig.getMdTenant().getTenantIdentifier();
        this.pmsIntervalMonths = mdServiceSchedulingConfig.getPmsIntervalMonths();
        this.pmcIntervalMonths = mdServiceSchedulingConfig.getPmcIntervalMonths();
        this.pmsIntervalKm = mdServiceSchedulingConfig.getPmsIntervalKm();
        this.pmcIntervalKm = mdServiceSchedulingConfig.getPmcIntervalKm();
        this.firstFreeServiceDueDaysFromPurchaseDate = mdServiceSchedulingConfig
                .getFirstFreeServiceDueDaysFromPurchaseDate();
        this.firstFreeServiceName = mdServiceSchedulingConfig.getFirstFreeServiceType().getServiceName();
        this.pmcServiceType = mdServiceSchedulingConfig.getPmcServiceType().getId();
        this.remindersEnabled = mdServiceSchedulingConfig.getRemindersEnabled();
    }
}
