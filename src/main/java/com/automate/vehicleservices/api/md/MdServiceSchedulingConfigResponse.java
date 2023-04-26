package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceSchedulingConfig;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;

import java.util.Objects;

@Data
public class MdServiceSchedulingConfigResponse implements MdResponse {
    private int id;
    private int firstFreeServiceTypeId;
    private String firstFreeServiceType;
    private int pmcServiceTypeId;
    private String pmcServiceType;
    private int pmsServiceCategoryId;
    private String pmsServiceCategory;
    private int pmcServiceCategoryId;
    private String pmcServiceCategory;
    private int freeServiceCategoryId;
    private String freeServiceCategory;
    private int pmsIntervalMonths;
    private int pmcIntervalMonths;
    private int pmsIntervalKm;
    private int pmcIntervalKm;
    private boolean remindersEnabled;
    private int firstFreeServiceDueDaysFromPurchaseDate;

    public MdServiceSchedulingConfigResponse(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        if (Objects.isNull(mdServiceSchedulingConfig))
            return;

        this.id = mdServiceSchedulingConfig.getId();
        firstFreeServiceTypeDetails(mdServiceSchedulingConfig);
        pmcServiceTypeDetails(mdServiceSchedulingConfig);
        pmcServiceCategoryDetails(mdServiceSchedulingConfig);
        pmsServiceCategoryDetails(mdServiceSchedulingConfig);
        freeServiceCategoryDetails(mdServiceSchedulingConfig);

        this.pmsIntervalMonths = mdServiceSchedulingConfig.getPmsIntervalMonths();
        this.pmcIntervalMonths = mdServiceSchedulingConfig.getPmcIntervalMonths();
        this.pmsIntervalKm = mdServiceSchedulingConfig.getPmsIntervalKm();
        this.pmcIntervalKm = mdServiceSchedulingConfig.getPmcIntervalKm();
        this.remindersEnabled = mdServiceSchedulingConfig.getRemindersEnabled();
        this.firstFreeServiceDueDaysFromPurchaseDate =
                mdServiceSchedulingConfig.getFirstFreeServiceDueDaysFromPurchaseDate();
    }

    private void firstFreeServiceTypeDetails(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        final var firstFreeServiceType = mdServiceSchedulingConfig.getFirstFreeServiceType();
        if (Objects.nonNull(firstFreeServiceType)) {
            this.firstFreeServiceTypeId = firstFreeServiceType.getId();
            this.firstFreeServiceType = firstFreeServiceType.getServiceName();
        }
    }

    private void pmcServiceTypeDetails(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        final var pmcServiceType = mdServiceSchedulingConfig.getPmcServiceType();
        if (Objects.nonNull(pmcServiceType)) {
            this.pmcServiceTypeId = pmcServiceType.getId();
            this.pmcServiceType = pmcServiceType.getServiceName();
        }
    }

    private void pmcServiceCategoryDetails(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        final var pmcServiceCategory = mdServiceSchedulingConfig.getPmcServiceCategory();
        if (Objects.nonNull(pmcServiceCategory)) {
            this.pmcServiceCategoryId = pmcServiceCategory.getId();
            this.pmcServiceCategory = pmcServiceCategory.getCategoryName();
        }
    }

    private void pmsServiceCategoryDetails(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        final var pmsServiceCategory = mdServiceSchedulingConfig.getPmsServiceCategory();
        if (Objects.nonNull(pmsServiceCategory)) {
            this.pmsServiceCategoryId = pmsServiceCategory.getId();
            this.pmsServiceCategory = pmsServiceCategory.getCategoryName();
        }
    }

    private void freeServiceCategoryDetails(MdServiceSchedulingConfig mdServiceSchedulingConfig) {
        final var freeServiceCategory = mdServiceSchedulingConfig.getFreeServiceCategory();

        if (Objects.nonNull(freeServiceCategory)) {
            this.freeServiceCategoryId = freeServiceCategory.getId();
            this.freeServiceCategory = freeServiceCategory.getCategoryName();
        }
    }
}
