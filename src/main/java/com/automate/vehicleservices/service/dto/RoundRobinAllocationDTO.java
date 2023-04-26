package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.RoundRobinDataAllocationStrategy;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RoundRobinAllocationDTO {

    private Integer id;

    private HRMSEmployee employee;

    private boolean isNumber;

    private List<ServiceTypeBasedAllocationRatioDTO> allocationRatioRequest;

    private ActiveInActiveStatus status;

    public RoundRobinAllocationDTO(RoundRobinDataAllocationStrategy details) {
        if (details == null)
            return;
        this.id = details.getId();
        this.employee = new HRMSEmployee(details.getEmployee());
        this.isNumber = details.getIsNumber();
        this.allocationRatioRequest = details.getAllocationRatioList().stream().map(data -> new ServiceTypeBasedAllocationRatioDTO(data)).collect(Collectors.toList());
        this.status = details.getStatus();
    }

    public RoundRobinAllocationDTO(HRMSEmployee details) {
        if (details == null)
            return;
        this.employee = details;
        this.isNumber = false;
        this.allocationRatioRequest = Collections.emptyList();
        this.status = ActiveInActiveStatus.INACTIVE;
    }
}
