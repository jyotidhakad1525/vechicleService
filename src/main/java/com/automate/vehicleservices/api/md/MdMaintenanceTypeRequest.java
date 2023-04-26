package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MdMaintenanceTypeRequest extends MdRequest {

    private String description;
    @NotBlank
    private String shortCode;
    @NotBlank
    @Size(max = 255)
    private String type;
}
