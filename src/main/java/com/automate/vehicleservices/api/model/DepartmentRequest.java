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
public class DepartmentRequest {

    private Integer id;

    @NotBlank(message = "Department name is mandatory!")
    private String departmentName;

    @NotNull(message = "Department value is not null!")
    private Integer departmentValue;

    private ActiveInActiveStatus status;
}
