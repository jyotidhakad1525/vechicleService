package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.CommunicationMode;
import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;

import java.util.Objects;

@Data
public class MdCommunicationModeResponse implements MdResponse {

    private int id;
    private CommunicationModeEnum communicationModeEnum;
    private Boolean active;

    public MdCommunicationModeResponse(CommunicationMode communicationMode) {
        if (Objects.isNull(communicationMode))
            return;

        this.communicationModeEnum = communicationMode.getType();
        this.id = communicationMode.getId();
        this.active = communicationMode.getActive();
    }
}
