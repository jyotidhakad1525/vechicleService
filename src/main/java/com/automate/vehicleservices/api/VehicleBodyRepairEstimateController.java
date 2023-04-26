package com.automate.vehicleservices.api;

import com.automate.vehicleservices.api.model.ServiceEstimateRequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.ServiceEstimateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Chandrashekar V
 */
@RestController
@RequestMapping(AbstractBaseController.BASE_URL + "/body-repair-estimate")
@Tag(name = "Body Repair Estimate", description = "Body Repair Estimate API")
public class VehicleBodyRepairEstimateController extends AbstractBaseController {

    private final ServiceEstimateService serviceEstimateService;

    public VehicleBodyRepairEstimateController(ServiceEstimateService serviceEstimateService) {
        this.serviceEstimateService = serviceEstimateService;
    }

    @PostMapping
    @Operation(summary = "Body Repair Estimate for the given Vehicle", description = "Service Estimate for the given " +
            "Vehicle and chosen service type. Captures customers information",
            tags = {"Body Repair Estimate"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    public ResponseEntity<APIResponse<?>> estimate(
            @RequestBody @Valid ServiceEstimateRequest request) {
        return responseEntity(serviceEstimateService.estimate(request, getTenant()));
    }

}
