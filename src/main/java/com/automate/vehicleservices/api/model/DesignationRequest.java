package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DesignationRequest {

    private Integer id;

    @NotBlank(message = "Designation name is mandatory!")
    private String designationName;

    @NotNull(message = "Designation value is not null!")
    private Integer designationValue;

    private ActiveInActiveStatus status;

    @NotNull(message = "Department value is not null!")
    private Integer departmentId;

}
