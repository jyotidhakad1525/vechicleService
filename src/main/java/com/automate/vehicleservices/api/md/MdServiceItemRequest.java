package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

@Data
public class MdServiceItemRequest extends MdRequest {

    private String description;
    private String name;
    private String shortCode;

}
