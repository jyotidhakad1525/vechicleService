package com.automate.vehicleservices.api;


import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.ServiceLogicReminderRequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.ServiceLogicReminderService;
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

@Tag(name = "Service Logic Reminder", description = "Service Logic Reminder API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/serviceLogicReminder")
@Validated
public class ServiceLogicReminderController extends AbstractBaseController {

    private final ServiceLogicReminderService service;

    public ServiceLogicReminderController(ServiceLogicReminderService service) {
        this.service = service;
    }


    @Operation(summary = "API to add new Service Logic Reminder and update existing Service Logic Reminder",
            description = "Add new Service Logic Reminder into the system.",
            tags = {"Service Logic Reminder"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/addOrUpdate")
    public ResponseEntity<APIResponse<?>> addOrUpdateServiceLogicReminder(
            @Valid @RequestBody ServiceLogicReminderRequest serviceLogicReminderRequest) {
        return responseEntity(service.addOrUpdateServiceLogicReminder(serviceLogicReminderRequest, getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to fetch list of Service Logic Reminder in the order of creation date",
            description = "List of Service Logic Reminder",
            tags = {"Service Logic Reminder"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/fetch-serviceLogicReminder")
    public ResponseEntity<APIResponse<?>> fetchAllServiceLogicReminder(Pagination pagination) {
        return responseEntity(service.fetchAllServiceLogicReminder(pagination, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to fetch Service Logic Reminder based on id",
            description = "Fetch Service Logic Reminder based on id",
            tags = {"Service Logic Reminder"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/fetch-serviceLogicReminder/{id}")
    public ResponseEntity<APIResponse<?>> fetchServiceLogicReminderBasedOnId(@PathVariable Integer id) {
        return responseEntity(service.fetchServiceLogicReminderBasedOnId(id, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to delete Service Logic Reminder based on id",
            description = "delete Service Logic Reminder based on id",
            tags = {"Service Logic Reminder"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/delete-serviceLogicReminder/{id}")
    public ResponseEntity<APIResponse<?>> deleteServiceLogicReminderBasedOnId(@PathVariable Integer id) {
        service.deleteServiceLogicReminderBasedOnId(id, getOrgBasedOnTenantId().getId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to Search for Service Logic Reminder Data",
            description = "Search for Service Logic Reminder",
            tags = {"Service Logic Reminder"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "serviceType"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "subServiceType"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "reminderDays"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "status"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/search")
    public ResponseEntity<APIResponse<?>> search(ServiceLogicReminderRequest request, Pagination pagination) {
        return responseEntity(service.search(request, pagination, getOrgBasedOnTenantId().getId()));
    }
}
