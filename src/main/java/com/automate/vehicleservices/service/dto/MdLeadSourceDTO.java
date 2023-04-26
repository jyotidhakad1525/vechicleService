package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.MdLeadSource;
import lombok.Getter;


@Getter
public class MdLeadSourceDTO {

    private int id;
    private String type;
    private MdLeadSourceDTO parent;

    public MdLeadSourceDTO(MdLeadSource leadSource) {
        if (leadSource == null)
            return;
        this.id = leadSource.getId();
        this.type = leadSource.getType();
        MdLeadSource parent = leadSource.getParentId();
        if (parent != null) {
            this.parent = new MdLeadSourceDTO(parent);
        }
    }
}
