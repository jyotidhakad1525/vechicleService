package com.automate.vehicleservices.api.v1;

import com.automate.vehicleservices.api.model.v1.VehicleServiceHistoryRequestV1;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.VehicleServiceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chandrashekar V
 */
@Tag(name = "Service History", description = "Service History API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/v1/service-history")
public class VehicleServiceHistoryV1Controller extends AbstractBaseController {

    private final VehicleServiceHistoryService vehicleServiceHistoryService;

    public VehicleServiceHistoryV1Controller(VehicleServiceHistoryService vehicleServiceHistoryService) {
        this.vehicleServiceHistoryService = vehicleServiceHistoryService;
    }


    @Operation(summary = "API to create service history",
            description = "Service History for the given vehicle by Tenant. This requires Service Type, Tenant",
            tags = {"Service history"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> createServiceHistoryV1(@PathVariable(TENANT) final String tenant,
                                                                 @Validated @RequestBody VehicleServiceHistoryRequestV1
                                                                         vehicleServiceHistoryRequest) {
        return responseEntity(vehicleServiceHistoryService.createServiceHistoryV1(vehicleServiceHistoryRequest,
                getTenant()));
    }


    @Operation(summary = "API to Update service history",
            description = "Service History for the given vehicle by Tenant. This requires Service Type, Tenant",
            tags = {"Service history"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @PutMapping
    public ResponseEntity<APIResponse<?>> UpdateServiceHistoryV1(@PathVariable(TENANT) final String tenant,
                                                                 @Validated @RequestBody VehicleServiceHistoryRequestV1
                                                                         vehicleServiceHistoryRequest) {
        return responseEntity(vehicleServiceHistoryService.UpdateServiceHistoryV1(vehicleServiceHistoryRequest,
                getTenantId()));
    }


    @Operation(summary = "API to Update service history",
            description = "Service History for the given vehicle by Tenant. This requires Service Type, Tenant",
            tags = {"Service history"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @GetMapping("/get-all-details/{regNumber}")
    public ResponseEntity<APIResponse<?>> getCustomerWithAllSectionDetails(@PathVariable(TENANT) final String tenant,
                                                                 @PathVariable("regNumber") String regNumber) {
        return responseEntity(vehicleServiceHistoryService.getCustomerWithAllSectionDetails(regNumber,
                getTenantId()));
    }

}
