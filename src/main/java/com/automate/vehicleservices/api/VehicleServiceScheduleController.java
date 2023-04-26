package com.automate.vehicleservices.api;

import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.VehicleServiceScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/**
 * @author Chandrashekar V
 */
@Tag(name = "Service Schedule", description = "Service Schedule API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/service-schedule")
public class VehicleServiceScheduleController extends AbstractBaseController {

    private final VehicleServiceScheduleService vehicleServiceScheduleService;

    public VehicleServiceScheduleController(VehicleServiceScheduleService vehicleServiceScheduleService) {
        this.vehicleServiceScheduleService = vehicleServiceScheduleService;
    }


    @Operation(summary = "API to fetch service schedule of all vehicles by tenant",
            description = "Service Schedule of all the vehicles of the given Tenant.",
            tags = {"Service schedule"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @GetMapping
    public ResponseEntity<APIResponse<?>> serviceScheduleByTenant(
            @RequestParam(required = false) ScheduleStatus[] scheduleStatuses) {

        if (Objects.nonNull(scheduleStatuses) && scheduleStatuses.length != 0)
            return responseEntity(vehicleServiceScheduleService.findSchedulesByTenantAndStatus(getTenant()
                    , ScheduleStatus.values()));

        // If no statuses requested explicitly, then return NEW and in progress schedules only
        return responseEntity(vehicleServiceScheduleService.findSchedulesByTenantAndStatus(getTenant()
                , ScheduleStatus.NEW, ScheduleStatus.IN_PROGRESS));
    }

    @Operation(summary = "API to fetch service schedule of a given vehicle",
            description = "Service Schedule of a given vehicle.",
            tags = {"Service schedule"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @GetMapping("/{vehicle-reg-number}")
    public ResponseEntity<APIResponse<?>> serviceScheduleByVehicle(
            @PathVariable("vehicle-reg-number") String vehicleRegNumber,
            @RequestParam(required = false) ScheduleStatus[] scheduleStatuses) {

        if (Objects.nonNull(scheduleStatuses) && scheduleStatuses.length != 0)
            return responseEntity(vehicleServiceScheduleService.findSchedulesByVehicleAndStatuses(vehicleRegNumber,
                    scheduleStatuses));

        return responseEntity(vehicleServiceScheduleService.findByVehicleWithActiveSchedule(vehicleRegNumber));
    }
}
