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
@Tag(name = "Service Estimate", description = "Service Estimate API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/service-estimate")
public class ServiceEstimateController extends AbstractBaseController {

    private final ServiceEstimateService serviceEstimateService;

    public ServiceEstimateController(ServiceEstimateService serviceEstimateService) {
        this.serviceEstimateService = serviceEstimateService;
    }


    @Operation(summary = "Service Estimate for the given Vehicle", description = "Service Estimate for the given " +
            "Vehicle and chosen service type. Captures customers information",
            tags = {"Service Estimate"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @PostMapping("/estimate")
    public ResponseEntity<APIResponse<?>> estimate(@Valid @RequestBody ServiceEstimateRequest estimateRequest) {
        return responseEntity(serviceEstimateService.estimate(estimateRequest, getTenant()));

    }
}
