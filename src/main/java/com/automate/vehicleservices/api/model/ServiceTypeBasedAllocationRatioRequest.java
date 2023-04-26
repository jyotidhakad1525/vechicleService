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
public class ServiceTypeBasedAllocationRatioRequest {

    @NotNull(message = "Service type id required!")
    private Integer serviceTypeId;
    @NotNull(message = "ratio is required!!")
    private Integer ratio;
}
