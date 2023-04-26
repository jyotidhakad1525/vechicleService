package com.automate.vehicleservices.api;

import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.VehicleServiceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chandrashekar V
 */
@Tag(name = "Service Type", description = "Service Types API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL)
public class VehicleServiceTypesController extends AbstractBaseController {

    private final VehicleServiceTypeService vehicleServiceTypeService;

    public VehicleServiceTypesController(VehicleServiceTypeService vehicleServiceTypeService) {
        this.vehicleServiceTypeService = vehicleServiceTypeService;
    }

    /**
     * Fetches additional services for the given tenant.
     *
     * @return
     */
    @Operation(summary = "Special API that returns additional services",
            description = "Returns additional services configured for the given service center (tenant)",
            tags = {"Service Type"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/service-types/additional-services")
    public ResponseEntity<APIResponse<?>> fetchAdditionalServices() {

        return responseEntity(vehicleServiceTypeService.fetchAdditionalServices(getTenantId(),
                ServiceGroup.QUICK_SERVICE));
    }

    @Operation(summary = "Special API that returns service categories",
            description = "Returns service categories service center (tenant)",
            tags = {"Service Type"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/service-categories")
    public ResponseEntity<APIResponse<?>> serviceCategories() {
        return responseEntity(vehicleServiceTypeService.activeServiceCategories(getTenant()));
    }

    @Operation(summary = "Special API that returns service types",
            description = "Returns service Types for the given service center (tenant)",
            tags = {"Service Type"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/service-types")
    public ResponseEntity<APIResponse<?>> serviceTypes() {
        return responseEntity(vehicleServiceTypeService.activeServiceTypes(getTenant()));
    }

}
