package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class MdServiceSchedulingConfigRequest extends MdRequest {
    @Positive
    private int firstFreeServiceTypeId;
    @Positive
    private int pmcServiceTypeId;
    @Positive
    private int pmsServiceCategoryId;
    @Positive
    private int pmcServiceCategoryId;
    @Positive
    private int freeServiceCategoryId;
    @Positive
    private int pmsIntervalMonths;
    @Positive
    private int pmcIntervalMonths;
    @Positive
    private int pmsIntervalKm;
    @Positive
    private int pmcIntervalKm;
    @NotNull
    private Boolean enableReminders;
    @Positive
    private int firstFreeServiceDueDaysFromPurchaseDate;
}
