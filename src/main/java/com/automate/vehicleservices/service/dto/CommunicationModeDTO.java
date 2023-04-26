package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.CommunicationMode;
import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationModeDTO {
    private CommunicationModeEnum type;
    private String tenant;
    private int id;

    public CommunicationModeDTO(CommunicationMode communicationMode) {
        if (communicationMode == null)
            return;

        this.type = communicationMode.getType();
        this.tenant = communicationMode.getMdTenant().getTenantIdentifier();
        this.id = communicationMode.getId();
    }
}
