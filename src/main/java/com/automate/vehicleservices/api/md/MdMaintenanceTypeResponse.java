package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdMaintenanceType;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;

import java.util.Objects;

@Data
public class MdMaintenanceTypeResponse implements MdResponse {

    private int id;
    private String description;
    private String shortCode;
    private String type;
    private Boolean active;

    public MdMaintenanceTypeResponse(MdMaintenanceType mdMaintenanceType) {
        if (Objects.isNull(mdMaintenanceType))
            return;

        this.id = mdMaintenanceType.getId();
        this.description = mdMaintenanceType.getDescription();
        this.shortCode = mdMaintenanceType.getShortCode();
        this.type = mdMaintenanceType.getType();
        this.active = mdMaintenanceType.getActive();
    }
}
