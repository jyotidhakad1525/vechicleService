package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdLeadSource;
import com.automate.vehicleservices.framework.api.MdResponse;
import com.automate.vehicleservices.repository.dtoprojection.MdLeadSourceHierarchyDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class MdLeadSourceResponse implements MdResponse {
    private int id;
    private String type;
    private Boolean active;
    private int parentId;
    private String parentType;

    public MdLeadSourceResponse(MdLeadSource mdLeadSource) {
        if (Objects.isNull(mdLeadSource))
            return;

        this.type = mdLeadSource.getType();
        this.id = mdLeadSource.getId();
        this.active = mdLeadSource.getActive();
        final var parent = mdLeadSource.getParentId();
        if (Objects.nonNull(parent)) {
            this.parentId = parent.getId();
            this.parentType = parent.getType();
        }
    }

    public MdLeadSourceResponse(MdLeadSourceHierarchyDTO leadSourceHierarchyDTO) {
        if (Objects.isNull(leadSourceHierarchyDTO))
            return;

        this.type = leadSourceHierarchyDTO.getType();
        this.id = leadSourceHierarchyDTO.getId();
        this.active =
                Objects.nonNull(leadSourceHierarchyDTO.getIsActive()) && leadSourceHierarchyDTO.getIsActive() == 1 ?
                        Boolean.TRUE : Boolean.FALSE;
        this.parentId = leadSourceHierarchyDTO.getParentId();
        this.parentType = leadSourceHierarchyDTO.getParentType();
    }

}
