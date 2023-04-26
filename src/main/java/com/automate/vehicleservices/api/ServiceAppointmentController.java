package com.automate.vehicleservices.api;

import com.automate.vehicleservices.api.model.AppointmentRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.ServiceAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Chandrashekar V
 */

@Tag(name = "Appointment", description = "Service Appointment API")
@RestController
@RequestMapping(AbstractBaseController.BASE_URL + "/appointment")
public class ServiceAppointmentController extends AbstractBaseController {

    private final ServiceAppointmentService appointmentService;

    public ServiceAppointmentController(ServiceAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "Schedule an Appointment", description = "Schedule a service appointment", tags = {
            "Appointment"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> schedule(@PathVariable("tenant") final String tenant,
                                                   @RequestBody @Valid AppointmentRequest appointmentRequest) {

        return responseEntity(appointmentService.bookAppointment(appointmentRequest, getTenant()));
    }

    @Operation(summary = "Schedule an Appointment", description = "Schedule a service appointment", tags = {
            "Appointment"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @PostMapping("/{id}/reschedule")
    public ResponseEntity<APIResponse<?>> reschedule(@PathVariable("tenant") final String tenant,
                                                     @PathVariable("id") final int id,
                                                     @RequestBody @Valid AppointmentRequest appointmentRequest) {

        return responseEntity(appointmentService.rescheduleAppointment(getTenant(), id, appointmentRequest));
    }


    @Operation(summary = "Cancels an Appointment", description = "Cancels a service appointment", tags = {
            "Appointment"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<?>> schedule(@PathVariable("tenant") final String tenant,
                                                   @PathVariable("id") final int id,
                                                   @RequestParam String reason,
                                                   @RequestParam(required = false) String remark) {

        appointmentService.cancelAppointment(getTenant(), id,reason,remark);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }


    @Operation(summary = "Fetch active Appointments", description = "Returns active appointments for today and later " +
            "for the given tenant. ",
            tags = {"Appointment"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping
    public ResponseEntity<APIResponse<?>> fetchActiveAppointments(@PathVariable("tenant") final String tenant,
                                                                  Pagination pagination) {

        return responseEntity(appointmentService.activeAppointments(getTenant(), pagination));
    }


    @Operation(summary = "Fetch active Appointments", description = "Returns active appointments for today and later " +
            "for the given vehicle and tenant ",
            tags = {"Appointment"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/{vehicleRegNumber}")
    public ResponseEntity<APIResponse<?>> fetchActiveAppointments(@PathVariable("tenant") final String tenant,
                                                                  @PathVariable("vehicleRegNumber") final String vehicle) {

        return responseEntity(appointmentService.activeAppointments(getTenant(), vehicle));
    }

}
