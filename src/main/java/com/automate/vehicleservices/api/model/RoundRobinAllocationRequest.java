package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
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
public class RoundRobinAllocationRequest {

    private Integer id;

    @NotNull(message = "employee id is required!!")
    private Integer employeeId;

    private boolean isNumber;

    @Valid
    private List<ServiceTypeBasedAllocationRatioRequest> allocationRatioRequest;

    private ActiveInActiveStatus status;
}
