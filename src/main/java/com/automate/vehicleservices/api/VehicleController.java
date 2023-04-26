package com.automate.vehicleservices.api;

import com.automate.vehicleservices.api.model.*;
import com.automate.vehicleservices.api.model.v1.VehicleDetailsV1;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.service.*;
import com.automate.vehicleservices.service.dto.ValidationError;
import com.automate.vehicleservices.service.facade.VehicleFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name = "Vehicle", description = "Vehicle API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/vehicle")
public class VehicleController extends AbstractBaseController {

    private final VehicleFacade vehicleFacade;

    private final ServiceVehicleService serviceVehicleService;

    private final VehicleKmTrackerService vehicleKmTrackerService;

    private final ExcelReaderService excelReaderService;

    private final VehicleInsuranceService vehicleInsuranceService;

    private final VehicleWarrantyService vehicleWarrantyService;

    public VehicleController(VehicleFacade vehicleFacade, ServiceVehicleService serviceVehicleService,
                             VehicleKmTrackerService vehicleKmTrackerService, ExcelReaderService excelReaderService,
                             VehicleInsuranceService vehicleInsuranceService,
                             VehicleWarrantyService vehicleWarrantyService) {
        this.vehicleFacade = vehicleFacade;
        this.serviceVehicleService = serviceVehicleService;
        this.vehicleKmTrackerService = vehicleKmTrackerService;
        this.excelReaderService = excelReaderService;
        this.vehicleInsuranceService = vehicleInsuranceService;
        this.vehicleWarrantyService = vehicleWarrantyService;
    }

    @Operation(summary = "API to list recent Customers in the order of creation date",
            description = "List recent customers",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "reg-number", description = "vehicle" +
                    "-reg=number")})
    @GetMapping("/{reg-number}/complete-info")
    public ResponseEntity<APIResponse<?>> fetchCompleteVehicleInfo(
            @PathVariable("reg-number") @Valid @NotBlank String vehicleRegNumber) {
        return responseEntity(serviceVehicleService.fetchCompleteVehicleInfo(getTenant(), vehicleRegNumber));
    }


    @Operation(summary = "API to add new Vehicle",
            description = "Add new vehicle into the system. Tenant information is mandatory. If vehicle already " +
                    "exists throws exception",
            tags = {"Vehicle"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewVehicle(@Validated @RequestBody VehicleRequest vehicleRequest) {
        return responseEntity(vehicleFacade.addNewVehicle(vehicleRequest, getTenant()));
    }

    @Operation(summary = "API to bulk upload Vehicles",
            description = "Add new vehicles into the system, along with customers. Tenant information is mandatory. ",
            tags = {"Vehicle"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping(value = "/bulk-upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<APIResponse<?>> bulkUploadVehicles(@RequestParam("file") MultipartFile file) {

        log.info(String.format("File type: %s", file.getContentType()));
        int total = 0;
        int failed = 0;
        try {
            final var read = excelReaderService.read(file.getInputStream(), getTenant());
            total = read.getTotalRecords();
            failed = read.getFailedRecords();
            List<ServiceVehicleDTO> successRecords = new ArrayList<>();
            for (VehicleRequest vehicleRequest : read.getVehicles()) {
                String exception = null;
                try {
                    final ServiceVehicleDTO serviceVehicleDTO = vehicleFacade.addNewVehicle(vehicleRequest,
                            getTenant());
                    // add Vehicle DTO to response
                    successRecords.add(serviceVehicleDTO);
                } catch (VehicleServicesException ex) {
                    // add exception to response
                    exception = ex.getMessage();
                }

                if (StringUtils.isNotBlank(exception)) {
                    read.addError(vehicleRequest.getVehicleDetails().getVehicleRegNumber(),
                            ValidationError.builder().error(exception).build());
                    failed++;
                }

            }
            final var bulkUploadResponse = BulkUploadResponse.builder()
                    .successRecords(successRecords)
                    .failedRecords(read.getErrorMap())
                    .total(total)
                    .successCount(total - failed)
                    .failedCount(failed).build();
            return responseEntity(bulkUploadResponse);

        } catch (IOException e) {
            throw new VehicleServicesException("Upload failed. Exception while reading file.");

        }

    }

    @Operation(summary = "API to update Vehicle KM reading",
            description = "Add new vehicle KM reading.", tags = {"Vehicle"}, parameters = {@Parameter(in =
            ParameterIn.PATH, required = true, name = TENANT)})
    @PatchMapping("/{reg-number}/km-tracker")
    public ResponseEntity<APIResponse<?>> updateVehicleKM(@PathVariable("reg-number") String vehicleRegNumber,
                                                          @Validated @RequestBody VehicleKMRequest vehicleKMRequest) {
        return responseEntity(vehicleKmTrackerService.addNewEntry(vehicleKMRequest, vehicleRegNumber));
    }

    @Operation(summary = "API to delete Vehicle",
            description = "Delete vehicle", tags = {"Vehicle"}, parameters =
            {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @DeleteMapping("/{reg-number}")
    public ResponseEntity<APIResponse<?>> deleteVehicle(@PathVariable("reg-number") @Valid @NotBlank String vehicleRegNumber) {
        serviceVehicleService.deleteVehicle(vehicleRegNumber);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }


    @Operation(summary = "API to delete Vehicle Insurance",
            description = "Delete vehicle's Insurance by given id", tags = {"Vehicle"}, parameters =
            {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @DeleteMapping("/{reg-number}/insurance/{insurance-id}")
    public ResponseEntity<APIResponse<?>> deleteVehicleInsurance(@PathVariable("reg-number") @Valid @NotBlank String vehicleRegNumber,
                                                                 @PathVariable("insurance-id") @Valid @NotBlank int insuranceId) {
        vehicleInsuranceService.deleteVehicleInsurance(vehicleRegNumber, insuranceId);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to delete Vehicle Warranty",
            description = "Delete vehicle's Warranty by given id", tags = {"Vehicle"}, parameters =
            {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @DeleteMapping("/{reg-number}/warranty/{warranty-id}")
    public ResponseEntity<APIResponse<?>> deleteVehicleWarranty(@PathVariable("reg-number") @Valid @NotBlank String vehicleRegNumber,
                                                                @PathVariable("warranty-id") @Valid @NotBlank int warrantyId) {
        vehicleWarrantyService.deleteVehicleWarranty(vehicleRegNumber, warrantyId);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to update Vehicle",
            description = "Update vehicle. Use this API only when entire Vehicle object is being passed. The passed " +
                    "values will override the existing vehicle values.", tags = {"Vehicle"}, parameters =
            {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PutMapping("/{reg-number}/warranty/{warranty-id}")
    public ResponseEntity<APIResponse<?>> updateVehicleWarranty(@PathVariable("reg-number") String vehicleRegNumber,
                                                                @PathVariable("warranty-id") @Valid @NotBlank int warrantyId,
                                                                @Validated @RequestBody VehicleWarrantyRequest vehicleWarrantyRequest) {
        return responseEntity(vehicleWarrantyService.updateVehicleWarranty(vehicleWarrantyRequest, vehicleRegNumber,
                warrantyId));
    }

    @Operation(summary = "API to update Vehicle",
            description = "Update vehicle. Use this API only when entire Vehicle object is being passed. The passed " +
                    "values will override the existing vehicle values.", tags = {"Vehicle"}, parameters =
            {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PutMapping("/{reg-number}/insurance/{insurance-id}")
    public ResponseEntity<APIResponse<?>> updateVehicleInsurance(@PathVariable("reg-number") String vehicleRegNumber,
                                                                 @PathVariable("insurance-id") @Valid @NotBlank int insuranceId,
                                                                 @Validated @RequestBody VehicleInsuranceUpdateRequest vehicleInsuranceUpdateRequest) {
        return responseEntity(vehicleInsuranceService.updateVehicleInsurance(vehicleInsuranceUpdateRequest,
                vehicleRegNumber, insuranceId));
    }


    @Operation(summary = "API to update Vehicle",
            description = "Update vehicle. Use this API only when entire Vehicle object is being passed. The passed " +
                    "values will override the existing vehicle values.", tags = {"Vehicle"}, parameters =
            {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PutMapping("/{reg-number}")
    public ResponseEntity<APIResponse<?>> updateVehicle(@PathVariable("reg-number") String vehicleRegNumber,
                                                        @Validated @RequestBody VehicleDetailsV1 vehicleDetails) {
        return responseEntity(serviceVehicleService.update(vehicleDetails, vehicleRegNumber));
    }

    @Operation(summary = "API to Fetch Vehicle",
            description = "Fetch vehicle. Vehicle registration number is mandatory.",
            tags = {"Vehicle"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/{reg-number}")
    public ResponseEntity<APIResponse<?>> fetchVehicle(@PathVariable("reg-number") String vehicleRegistrationNumber) {
        return responseEntity(serviceVehicleService.findByVehicleRegNumber(vehicleRegistrationNumber));
    }

    @Operation(summary = "API to Fetch Vehicle Insurance",
            description = "Fetch vehicle Insurance. Vehicle registration number is mandatory.",
            tags = {"Vehicle"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/{reg-number}/insurance")
    public ResponseEntity<APIResponse<?>> fetchVehicleInsurance(@PathVariable("reg-number") String vehicleRegistrationNumber) {
        final var vehicleInsuranceMinimalDTO = serviceVehicleService.fetchVehicleInsurance(vehicleRegistrationNumber);
        if (Objects.isNull(vehicleInsuranceMinimalDTO))
            return new ResponseEntity(emptySuccessResponse(), HttpStatus.OK);
        return responseEntity(vehicleInsuranceMinimalDTO);
    }

    @Operation(summary = "API to Fetch Vehicle Warranties",
            description = "Fetch vehicle Warranties. Vehicle registration number is mandatory.",
            tags = {"Vehicle"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/{reg-number}/warranty")
    public ResponseEntity<APIResponse<?>> fetchVehicleWarranties(@PathVariable("reg-number") String vehicleRegistrationNumber) {
        return responseEntity(vehicleWarrantyService.fetchWarrantiesByVehicleRegNumber(vehicleRegistrationNumber));
    }

    @Operation(summary = "API to add Vehicle Insurance Information, to existing vehicle",
            description = "Add insurance to existing vehicle. Vehicle registration number is mandatory.",
            tags = {"Vehicle"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/{reg-number}/insurance/")
    public ResponseEntity<APIResponse<?>> addInsuranceDetails(
            @PathVariable("reg-number") String vehicleRegistrationNumber,
            @Validated @RequestBody VehicleInsuranceRequest insuranceRequest) {
        return responseEntity(vehicleFacade.addInsuranceDetails(getTenant(), insuranceRequest,
                vehicleRegistrationNumber));
    }

    @Operation(summary = "API to add Vehicle Vehicle Warranty Information, to existing vehicle",
            description = "Add insurance to existing vehicle. Vehicle registration number is mandatory.",
            tags = {"Vehicle"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/{reg-number}/warranty/")
    public ResponseEntity<APIResponse<?>> addWarrantyDetails(
            @PathVariable("reg-number") String vehicleRegistrationNumber,
            @Validated @RequestBody VehicleWarrantyRequest warrantyRequest) {
        return responseEntity(vehicleFacade.addWarrantyDetails(getTenant(), warrantyRequest,
                vehicleRegistrationNumber));
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class BulkUploadResponse {

        private List<ServiceVehicleDTO> successRecords;
        private Map<String, List<ValidationError>> failedRecords;
        private int total;
        private int successCount;
        private int failedCount;
    }
}
