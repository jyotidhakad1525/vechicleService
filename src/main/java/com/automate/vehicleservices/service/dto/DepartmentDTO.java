package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.DepartmentDetail;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DepartmentDTO {
    private Integer id;

    private String departmentName;

    private int departmentValue;

    private ActiveInActiveStatus status;

    public DepartmentDTO(DepartmentDetail departmentDetail) {
        if (Objects.isNull(departmentDetail))
            return;

        this.id = departmentDetail.getId();
        this.departmentName = departmentDetail.getDepartmentName();
        this.departmentValue = departmentDetail.getDepartmentValue();
        this.status = departmentDetail.getStatus();
    }
}
