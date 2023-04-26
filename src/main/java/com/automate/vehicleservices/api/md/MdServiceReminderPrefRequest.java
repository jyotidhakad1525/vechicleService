package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;
import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class MdServiceReminderPrefRequest extends MdRequest {

    @PositiveOrZero
    private int durationValue;
    @NotNull
    private Expression expression;
    @NotNull
    private TimeFrame timeFrame;
    @NotNull
    private Boolean isActive;
    @Positive
    private int serviceType;
    @Positive
    private int communicationMode;

}
