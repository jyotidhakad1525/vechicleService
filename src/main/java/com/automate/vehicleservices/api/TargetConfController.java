package com.automate.vehicleservices.api;


import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.TargetConfigurationRequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.TargetConfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Target Configuration", description = "Target Configuration API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/targetConf")
@Validated
public class TargetConfController extends AbstractBaseController {

    private final TargetConfService targetConfService;

    public TargetConfController(TargetConfService targetConfService) {
        this.targetConfService = targetConfService;
    }


    @Operation(summary = "API to add new Target Configuration and update existing Target Configuration",
            description = "Add new Target Configuration into the system.",
            tags = {"Target Configuration"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/addOrUpdate")
    public ResponseEntity<APIResponse<?>> addOrUpdateTargetConf(
            @Valid @RequestBody TargetConfigurationRequest targetConfigurationRequest) {
        return responseEntity(targetConfService.addOrUpdateTargetConf(targetConfigurationRequest, getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to fetch list of Target Configuration in the order of creation date",
            description = "List of Target Configuration",
            tags = {"Target Configuration"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/fetch-targetConf")
    public ResponseEntity<APIResponse<?>> fetchAllTargetConf(Pagination pagination) {
        return responseEntity(targetConfService.fetchAllTargetConf(pagination, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to fetch Target Configuration based on target id",
            description = "Fetch Target Configuration based on id",
            tags = {"Target Configuration"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/fetch-targetConf/{id}")
    public ResponseEntity<APIResponse<?>> fetchTargetConfBasedOnId(@PathVariable Integer id) {
        return responseEntity(targetConfService.fetchTargetConfBasedOnId(id, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to delete Target Configuration based on target id",
            description = "Delete Target Configuration based on id",
            tags = {"Target Configuration"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/delete-targetConf/{id}")
    public ResponseEntity<APIResponse<?>> deleteTargetConfBasedOnId(@PathVariable Integer id) {
        targetConfService.deleteTargetConfBasedOnId(id, getOrgBasedOnTenantId().getId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }


}
