package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MdServiceTypeRequest extends MdRequest {
    List<Integer> serviceItems;
    private String description;
    private String serviceCode;
    @NotBlank
    private String serviceName;
    @Positive
    private int serviceCategoryId;
    @PositiveOrZero
    private int pmsSequence;
}
