package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class MdServiceTypeCriteriaRequest extends MdRequest {
    @NotBlank
    private String description;
    @PositiveOrZero
    private int durationDaysFrom;
    @Positive
    private int durationDaysTo;
    @PositiveOrZero
    private int mileageFrom;
    @Positive
    private int mileageTo;
    @Positive
    private int serviceTypeId;
}
