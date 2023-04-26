package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceItem;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;

import java.util.Objects;

@Data
public class MdServiceItemResponse implements MdResponse {

    private int id;
    private String name;
    private String shortCode;
    private String description;

    public MdServiceItemResponse(MdServiceItem mdServiceItem) {
        if (Objects.isNull(mdServiceItem))
            return;

        this.id = mdServiceItem.getId();
        this.description = mdServiceItem.getDescription();
        this.name = mdServiceItem.getName();
    }
}
