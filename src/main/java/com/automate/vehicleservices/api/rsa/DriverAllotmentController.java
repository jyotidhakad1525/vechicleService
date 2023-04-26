package com.automate.vehicleservices.api.rsa;

import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.rsa.DriverAllotmentService;
import com.automate.vehicleservices.service.dto.DriverAllotmentModel;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(/*value = AbstractBaseController.BASE_URL + */"/drivers-allotment")
public class DriverAllotmentController extends AbstractBaseController {

    private final DriverAllotmentService allotmentService;

    public DriverAllotmentController(DriverAllotmentService allotmentService) {
        this.allotmentService = allotmentService;
    }

    @Operation(summary = "To Create and Update Allotments")
    @PostMapping
    public ResponseEntity<APIResponse<?>> saveAllotment(@RequestBody DriverAllotmentModel allotment) {
        return responseEntity(allotmentService.saveAllotment(allotment));
    }

    @Operation(summary = "List All allotments based on Org and Branch, Driver and Appointment Id's are Optional")
    @GetMapping("/all")
    public ResponseEntity<APIResponse<?>> getAllotments(
            @RequestParam(value = "driverId", required = false, defaultValue = "0") int driverId,
            @RequestParam(value = "appointmentId", required = false, defaultValue = "0") int appointmentId,
            @RequestParam(value = "orgId") int orgId, @RequestParam("branchId") int branchId) {
        return responseEntity(allotmentService.getAllotments(orgId, branchId, driverId, appointmentId));
    }

    @Operation(summary = "Delete Allotment by Allotment Id")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<APIResponse<?>> deleteAllotment(@PathVariable("id") int id) {
        allotmentService.deleteAllotment(id);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }
}