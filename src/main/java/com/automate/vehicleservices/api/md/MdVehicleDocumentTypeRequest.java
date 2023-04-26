package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MdVehicleDocumentTypeRequest extends MdRequest {

    @NotBlank
    private String documentName;
    @NotBlank
    private String label;
    private String description;
}
