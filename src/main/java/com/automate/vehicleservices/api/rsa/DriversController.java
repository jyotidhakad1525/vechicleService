package com.automate.vehicleservices.api.rsa;

import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.rsa.DriverService;
import com.automate.vehicleservices.service.dto.DriverModel;
import com.automate.vehicleservices.service.dto.RsaAppointment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Drivers API", description = "Drivers API ")
@RequestMapping(/*value = AbstractBaseController.BASE_URL + */"/drivers")
public class DriversController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(DriversController.class);

    private final DriverService driversService;

    public DriversController(DriverService driversService) {
        this.driversService = driversService;
    }

    @Operation(summary = "List All drivers based on Org and Branch", description = "Admin Panel List All Drivers")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<APIResponse<?>> getAllDrivers(@RequestParam("orgId") int orgId,
                                                        @RequestParam("branchId") int branchId) {
        logger.info("List all drivers of branch:: " + branchId);
        return responseEntity(driversService.getAllDriversList(orgId, branchId));
    }


    @Operation(summary = "List all drivers by time slot")
    @GetMapping(value = "/by-time-slot")
    public ResponseEntity<APIResponse<?>> fetchDriverForAppointment(RsaAppointment rsaAppointment) {
        logger.info("Fetch Driver for time slot");
        return responseEntity(driversService.fetchDriver(rsaAppointment));
    }

    @Operation(summary = "Save driver", description = "Admin Panel Diver Create and Update")
    @PostMapping
    public ResponseEntity<APIResponse<?>> saveDriver(@RequestBody DriverModel driver) {
        return responseEntity(driversService.saveDriver(driver));
    }

    @Operation(summary = "Delete Driver entry by Id", description = "Admin Panel Driver Delete")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<APIResponse<?>> deleteDriver(@PathVariable("id") int id) {
        driversService.deleteDriver(id);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

}
