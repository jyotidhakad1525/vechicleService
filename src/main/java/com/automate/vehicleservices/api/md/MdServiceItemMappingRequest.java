package com.automate.vehicleservices.api.md;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MdServiceItemMappingRequest {
    @NotNull
    private List<Integer> serviceItems;
}
