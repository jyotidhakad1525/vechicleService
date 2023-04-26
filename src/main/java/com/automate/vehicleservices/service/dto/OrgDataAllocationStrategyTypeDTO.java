package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.enums.AllocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgDataAllocationStrategyTypeDTO {

    private Integer id;

    private AllocationType allocationType;
}
