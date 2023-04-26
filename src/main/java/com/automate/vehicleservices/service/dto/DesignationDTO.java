package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.DesignationDetail;
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
public class DesignationDTO {
    private Integer id;

    private String designationName;

    private int designationValue;

    private ActiveInActiveStatus status;

    private DepartmentDTO departmentDetails;

    public DesignationDTO(DesignationDetail designationDetail) {
        if (Objects.isNull(designationDetail))
            return;

        this.id = designationDetail.getId();
        this.designationName = designationDetail.getDesignationName();
        this.designationValue = designationDetail.getDesignationValue();
        this.status = designationDetail.getStatus();
        this.departmentDetails = new DepartmentDTO(designationDetail.getDepartmentDetail());
    }
}
