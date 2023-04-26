package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.api.model.TargetConfParameter;
import com.automate.vehicleservices.entity.TargetConfigurationDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TargetConfDTO {
    private Integer id;

    private String dealerId;

    private CommonIdNameDTO departmentDetail;

    private CommonIdNameDTO designationDetail;

    private List<TargetConfParameter> targetConfParameter;

    public TargetConfDTO(TargetConfigurationDetails details) {
        if (Objects.isNull(details))
            return;

        this.id = details.getId();
        this.dealerId = details.getDealerId();
        this.departmentDetail = new CommonIdNameDTO(details.getDepartmentDetail().getId(), details.getDepartmentDetail().getDepartmentName());
        this.designationDetail = new CommonIdNameDTO(details.getDesignationDetail().getId(), details.getDesignationDetail().getDesignationName());
        this.targetConfParameter = details.getTargetConfigurationParameterDetails().stream().map(data -> new TargetConfParameter(data.getId(), data.getParameterName(), data.getTarget(), data.getUnit())).collect(Collectors.toList());
    }
}

