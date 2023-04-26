package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

@Data
public class MdCommunicationModeRequest extends MdRequest {

    private CommunicationModeEnum communicationModeEnum;
    private Boolean active;

}
