package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdVehicleDocumentType;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;

import java.util.Objects;

@Data
public class MdVehicleDocumentTypeResponse implements MdResponse {
    private int id;
    private String documentName;
    private String label;
    private String description;

    public MdVehicleDocumentTypeResponse(MdVehicleDocumentType mdVehicleDocumentType) {
        if (Objects.isNull(mdVehicleDocumentType))
            return;

        this.id = mdVehicleDocumentType.getId();
        this.documentName = mdVehicleDocumentType.getDocumentName();
        this.label = mdVehicleDocumentType.getLabel();
        this.description = mdVehicleDocumentType.getDescription();
    }
}
