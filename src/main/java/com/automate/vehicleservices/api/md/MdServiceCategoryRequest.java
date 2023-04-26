package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MdServiceCategoryRequest extends MdRequest {
    private String categoryName;
    private ServiceGroup serviceGroup;
}
