package com.automate.vehicleservices.api;


import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.ServiceFollowUpReasonRequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.ServiceFollowUpReasonService;
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

@Tag(name = "Service Follow-Up Reason", description = "Service Follow-Up Reason API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/serviceFollowUpReason")
@Validated
public class ServiceFollowUpReasonController extends AbstractBaseController {

    private final ServiceFollowUpReasonService service;

    public ServiceFollowUpReasonController(ServiceFollowUpReasonService service) {
        this.service = service;
    }


    @Operation(summary = "API to add new Service Follow-Up Reason and update existing Service Follow-Up Reason",
            description = "Add new Service Follow-Up Reason into the system.",
            tags = {"Service Follow-Up Reason"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/addOrUpdate")
    public ResponseEntity<APIResponse<?>> addOrUpdateServiceFollowUpReason(
            @Valid @RequestBody ServiceFollowUpReasonRequest serviceFollowUpReasonRequest) {
        return responseEntity(service.addOrUpdateServiceFollowUpReason(serviceFollowUpReasonRequest, getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to fetch list of Service Follow-Up Reason in the order of creation date",
            description = "List of Service Follow-Up Reason",
            tags = {"Service Follow-Up Reason"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/fetch-serviceFollowUpReason")
    public ResponseEntity<APIResponse<?>> fetchAllServiceFollowUpReason(Pagination pagination) {
        return responseEntity(service.fetchAllServiceFollowUpReason(pagination, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to fetch Service Follow-Up Reason based on id",
            description = "Fetch Service Follow-Up Reason based on id",
            tags = {"Service Follow-Up Reason"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/fetch-serviceFollowUpReason/{id}")
    public ResponseEntity<APIResponse<?>> fetchServiceFollowUpReasonBasedOnId(@PathVariable Integer id) {
        return responseEntity(service.fetchServiceFollowUpReasonBasedOnId(id, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to delete Service Follow-Up Reason based on id",
            description = "delete Service Follow-Up Reason based on id",
            tags = {"Service Follow-Up Reason"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/delete-serviceFollowUpReason/{id}")
    public ResponseEntity<APIResponse<?>> deleteServiceFollowUpReasonBasedOnId(@PathVariable Integer id) {
        service.deleteServiceFollowUpReasonBasedOnId(id, getOrgBasedOnTenantId().getId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to Search for Service Follow-Up Reason Data",
            description = "Search for Service Follow-Up Reason",
            tags = {"Service Follow-Up Reason"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "serviceType"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "subServiceType"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "reason"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "status"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/search")
    public ResponseEntity<APIResponse<?>> search(ServiceFollowUpReasonRequest request, Pagination pagination) {
        return responseEntity(service.search(request, pagination, getOrgBasedOnTenantId().getId()));
    }
}
