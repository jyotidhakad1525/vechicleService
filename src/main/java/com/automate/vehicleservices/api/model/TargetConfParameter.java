package com.automate.vehicleservices.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TargetConfParameter {
    private Integer id;
    
    @NotNull(message = "parameterName is required!!")
    private String parameterName;

    @NotNull(message = "target is required!!")
    private Integer target;

    @NotNull(message = "unit is required!!")
    private Integer unit;
}
