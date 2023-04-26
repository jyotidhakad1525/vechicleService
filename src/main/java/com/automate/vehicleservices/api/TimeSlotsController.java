package com.automate.vehicleservices.api;

import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.ServiceSlotAvailabilityService;
import com.automate.vehicleservices.service.dto.SlotAvailabilityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @author Chandrashekar V
 */
@Tag(name = "Time slot", description = "Time slot API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/time-slots")
@Validated
public class TimeSlotsController extends AbstractBaseController {


    private final ServiceSlotAvailabilityService slotAvailabilityService;

    public TimeSlotsController(ServiceSlotAvailabilityService slotAvailabilityService) {
        this.slotAvailabilityService = slotAvailabilityService;
    }

    /**
     * Fetches available time slots for the given tenant, for all the service types. Defaults to TODAY's date.
     *
     * @param date
     * @return
     */
    @Operation(summary = "Provides Time Slots for the chosen service date",
            description = "Returns service slots availability for the given service center and chosen service date",
            tags = {"Time slot"},
            parameters = {@Parameter(in = ParameterIn.QUERY, name = "date", description =
                    "Date for which time slots are being requested (yyyy-MM-dd).", allowEmptyValue = true, schema =
            @Schema(format = "yyyy-MM-dd")), @Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/service-date")
    public ResponseEntity<APIResponse<?>> fetchAvailableSlotsByTenant(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent LocalDate date) {
        if (Objects.isNull(date))
            date = LocalDate.now();
        List<SlotAvailabilityDTO> availabilityBYServiceTypes =
                slotAvailabilityService
                        .timeSlotAvailabilityByTenantAndDate(getTenantId(), date);
        return responseEntity(availabilityBYServiceTypes);
    }

}
