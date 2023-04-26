package com.automate.vehicleservices.service.dto;


import com.automate.vehicleservices.entity.MdVehicleDocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MdVehicleDocumentTypeDTO {

    private String documentName;

    private String label;

    private int id;

    public MdVehicleDocumentTypeDTO(MdVehicleDocumentType mdVehicleDocumentType) {
        if (Objects.isNull(mdVehicleDocumentType)) {
            new MdVehicleDocumentTypeDTO();
            return;
        }

        this.documentName = mdVehicleDocumentType.getDocumentName();
        this.label = mdVehicleDocumentType.getLabel();
        this.id = mdVehicleDocumentType.getId();
    }
}
