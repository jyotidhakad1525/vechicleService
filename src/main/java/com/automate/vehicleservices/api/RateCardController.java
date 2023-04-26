package com.automate.vehicleservices.api;

import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.MdServiceRateCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * @author Chandrashekar V
 */
@Tag(name = "Rate Card", description = "Rate Card API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/rate-card")
public class RateCardController extends AbstractBaseController {


    private final MdServiceRateCardService mdServiceRateCardService;

    public RateCardController(MdServiceRateCardService mdServiceRateCardService) {
        this.mdServiceRateCardService = mdServiceRateCardService;
    }

    @Operation(summary = "Rate Card by Vehicle Model, Variant and Service Type", description =
            "Given the Vehicle Model and Variant, responds with Rate Card for the chosen Service Type and Service " +
                    "center", tags = {"Rate Card"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true,
            name = TENANT)})

    @GetMapping("/{vehicle_model}/variant/{variant}/service/{service_type_id}")
    public ResponseEntity<APIResponse<?>> fetchRateCard(
            @PathVariable("vehicle_model") @Valid @NotBlank String vehicleModel,
            @PathVariable("variant") @Valid @NotBlank FuelType variant,
            @PathVariable("service_type_id") @Valid @Positive int serviceTypeId) {


        return responseEntity(mdServiceRateCardService
                .fetchRateCardByTenantAndVehicleAndServiceType(getTenantId(), vehicleModel, variant, serviceTypeId));

    }

    @Operation(summary = "Rate Card for all the configured services for the given Vehicle Model and Variant ",
            description =
                    "Given the Vehicle Model and Variant, responds with Rate Card for all the services ", tags = {
            "Rate Card"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})

    @GetMapping("/{vehicle_model}/variant/{variant}")
    public ResponseEntity<APIResponse<?>> fetchRateCardByTenant(
            @PathVariable("vehicle_model") @Valid @NotBlank String vehicleModel,
            @PathVariable("variant") @Valid @NotBlank FuelType variant) {
        return responseEntity(mdServiceRateCardService
                .fetchRateCardByTenantAndVehicle(getTenantId(), vehicleModel, variant));


    }
}
