package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
public class MdLeadSourceHierarchyResponse implements MdResponse {
    Boolean active;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<Integer, MdLeadSourceHierarchyResponse> subtypeMap;
    private int id;
    private String type;

    public MdLeadSourceHierarchyResponse(int id, String type, Integer active) {
        this.id = id;
        this.type = type;
        this.active = Objects.nonNull(active) && active == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    public void addSubType(MdLeadSourceHierarchyResponse mdLeadSourceHierarchyResponse) {

        getSubtypeMap().put(mdLeadSourceHierarchyResponse.getId(), mdLeadSourceHierarchyResponse);
    }

    public Map<Integer, MdLeadSourceHierarchyResponse> getSubtypeMap() {
        if (Objects.isNull(subtypeMap))
            subtypeMap = new HashMap<>();
        return subtypeMap;
    }
}
