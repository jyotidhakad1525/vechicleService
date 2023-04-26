package com.automate.vehicleservices.api;

import com.automate.vehicleservices.api.model.RoundRobinAllocationRequest;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.RoundRobinDataAllocationStrategyService;
import com.automate.vehicleservices.service.dto.HRMSEmployee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Round Robin Data Allocation", description = "Round Robin Data Allocation API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/rrdAllocation")
@AllArgsConstructor
@Validated
public class RoundRobinDataAllocationStrategyController extends AbstractBaseController {

    private final RoundRobinDataAllocationStrategyService service;

    @Operation(summary = "API to allocate data",
            description = "API to allocate data to cre",
            tags = {"Round Robin Data Allocation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> allocationToCre(@Valid @RequestBody RoundRobinAllocationRequest request) {
        return responseEntity(service.allocationToCre(request, getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to get allocated data",
            description = "API to get allocated data",
            tags = {"Round Robin Data Allocation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllAllocatedData() {
        return responseEntity(service.getAllAllocatedData(getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to get allocated data based on id",
            description = "API to get allocated data based on id",
            tags = {"Round Robin Data Allocation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<?>> getAllocatedDataBasedOnId(@PathVariable Integer id) {
        return responseEntity(service.getAllocatedDataBasedOnId(id, getOrgBasedOnTenantId()));
    }


    @Operation(summary = "API to update status based on id",
            description = "API to update status isNumber based on id",
            tags = {"Round Robin Data Allocation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id"),
            @Parameter(in = ParameterIn.QUERY, required = true, name = "changeStatus")})
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<?>> updateStatusBasedOnId(@PathVariable Integer id,
                                                                @RequestParam ActiveInActiveStatus changeStatus) {
        return responseEntity(service.updateStatusBasedOnId(changeStatus, id, getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to update isNumber",
            description = "API to update isNumber",
            tags = {"Round Robin Data Allocation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = true, name = "isNumber")})
    @PutMapping
    public ResponseEntity<APIResponse<?>> updateIsNumberBasedOnOrgId(
            @RequestParam Boolean isNumber) {
        service.updateIsNumberBasedOnOrgId(isNumber, getOrgBasedOnTenantId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

}
