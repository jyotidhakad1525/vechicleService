package com.automate.vehicleservices.api;

import com.automate.vehicleservices.entity.enums.AllocationType;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.OrgDataAllocationStrategyTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Organization Allocation Type", description = "Organization Allocation Type API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/allocationType")
@AllArgsConstructor
public class OrgDataAllocationStrategyTypeController extends AbstractBaseController {

    private final OrgDataAllocationStrategyTypeService service;

    @Operation(summary = "API to change allocation Type",
            description = "API to change allocation Type based on org id",
            tags = {"Organization Allocation Type"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = true, name = "changeType")})
    @PutMapping("/changeAllocation")
    public ResponseEntity<APIResponse<?>> changeAllocation(
            @RequestParam(name = "changeType") AllocationType allocationType) {
        service.changeAllocation(allocationType, getOrgBasedOnTenantId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to get active allocation Type",
            description = "API to get active allocation Type based on org id",
            tags = {"Organization Allocation Type"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/active")
    public ResponseEntity<APIResponse<?>> getActiveAllocation() {
        return responseEntity(service.getActiveAllocation(getOrgBasedOnTenantId()));
    }
}
