package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MdServiceRateCardRequest extends MdRequest {
    private Boolean applicableForDiesel;
    private Boolean applicableForElectric;
    private Boolean applicableForHybrid;
    private Boolean applicableForPetrol;

    @PositiveOrZero
    private double rate;
    @NotBlank
    private String vehicleModel;

    private int mdMaintenanceType;
    @Positive
    private int mdServiceItem;
    @Positive
    private int mdServiceType;

}
