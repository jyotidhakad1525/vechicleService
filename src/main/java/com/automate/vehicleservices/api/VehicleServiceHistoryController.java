package com.automate.vehicleservices.api;

import com.automate.vehicleservices.api.model.VehicleServiceHistoryRequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.VehicleServiceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chandrashekar V
 */
@Tag(name = "Service History", description = "Service History API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/service-history")
public class VehicleServiceHistoryController extends AbstractBaseController {

    private final VehicleServiceHistoryService vehicleServiceHistoryService;

    public VehicleServiceHistoryController(VehicleServiceHistoryService vehicleServiceHistoryService) {
        this.vehicleServiceHistoryService = vehicleServiceHistoryService;
    }


    @Operation(summary = "API to create service history",
            description = "Service History for the given vehicle by Tenant. This requires Service Type, Tenant",
            tags = {"Service history"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> createServiceHistory(@PathVariable(TENANT) final String tenant,
                                                               @Validated @RequestBody VehicleServiceHistoryRequest
                                                                       vehicleServiceHistoryRequest) {
        return responseEntity(vehicleServiceHistoryService.createServiceHistory(vehicleServiceHistoryRequest,
                getTenant()));
    }

    @Operation(summary = "API to fetch service history for the given vehicle",
            description = "Service History for the given vehicle.",
            tags = {"Service history"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @GetMapping("/{vehicle-reg-number}")
    public ResponseEntity<APIResponse<?>> getServiceHistory(@PathVariable(TENANT) final String tenant, @PathVariable(
            "vehicle-reg-number") String vehicleRegNumber) {
        return responseEntity(vehicleServiceHistoryService.fetchServiceHistory(vehicleRegNumber));
    }


    // TODO - bulk upload
}
