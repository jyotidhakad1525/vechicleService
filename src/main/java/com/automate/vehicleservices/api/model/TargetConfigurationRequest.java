package com.automate.vehicleservices.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TargetConfigurationRequest {

    private Integer id;

    @NotNull(message = "dealerId must be required!")
    private String dealerId;

    @NotNull(message = "departmentId must be required!")
    private Integer departmentId;

    @NotNull(message = "designationId must be required!")
    private Integer designationId;

    //    @NotNull(message = "parameter must be required!")
    @Valid
    private List<TargetConfParameter> targetConfParameter;
}
