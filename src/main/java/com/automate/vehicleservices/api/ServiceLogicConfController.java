package com.automate.vehicleservices.api;


import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.ServiceLogicConfRequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.ServiceLogicConfService;
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

@Tag(name = "Service Logic Configuration", description = "Service Logic Configuration API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/serviceLogicConf")
@Validated
public class ServiceLogicConfController extends AbstractBaseController {

    private final ServiceLogicConfService serviceLogicConfService;

    public ServiceLogicConfController(ServiceLogicConfService serviceLogicConfService) {
        this.serviceLogicConfService = serviceLogicConfService;
    }

    @Operation(summary = "API to add new Service Logic Configuration and update existing Service Logic Configuration",
            description = "Add new Service Logic Configuration into the system.",
            tags = {"Service Logic Configuration"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/addOrUpdate")
    public ResponseEntity<APIResponse<?>> addOrUpdateServiceLogicConf(
            @Valid @RequestBody ServiceLogicConfRequest serviceLogicConfRequest) {
        return responseEntity(serviceLogicConfService.addOrUpdateServiceLogicConf(serviceLogicConfRequest, getOrgBasedOnTenantId()));
    }


    @Operation(summary = "API to fetch list of Service Logic Configuration in the order of creation date",
            description = "List of Service Logic Configuration",
            tags = {"Service Logic Configuration"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/fetch-serviceLogicConf")
    public ResponseEntity<APIResponse<?>> fetchAllServiceLogicConf(Pagination pagination) {
        return responseEntity(serviceLogicConfService.fetchAllServiceLogicConf(pagination, getOrgBasedOnTenantId().getId()));
    }


    @Operation(summary = "API to fetch Service Logic Configuration based on id",
            description = "Fetch Service Logic Configuration based on id",
            tags = {"Service Logic Configuration"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/fetch-serviceLogicConf/{id}")
    public ResponseEntity<APIResponse<?>> fetchServiceLogicConfBasedOnId(@PathVariable Integer id) {
        return responseEntity(serviceLogicConfService.fetchServiceLogicConfBasedOnId(id, getOrgBasedOnTenantId().getId()));
    }


    @Operation(summary = "API to delete Service Logic Configuration based on id",
            description = "delete Service Logic Configuration based on id",
            tags = {"Service Logic Configuration"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/delete-serviceLogicConf/{id}")
    public ResponseEntity<APIResponse<?>> deleteServiceLogicConfBasedOnId(@PathVariable Integer id) {
        serviceLogicConfService.deleteServiceLogicConfBasedOnId(id, getOrgBasedOnTenantId().getId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to Search for Service Logic Configuration Data",
            description = "Search for Service Logic Configuration",
            tags = {"Service Logic Configuration"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "serviceType"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "status"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/search")
    public ResponseEntity<APIResponse<?>> search(ServiceLogicConfRequest request, Pagination pagination) {
        return responseEntity(serviceLogicConfService.search(request, pagination, getOrgBasedOnTenantId().getId()));
    }
}
