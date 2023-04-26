package com.automate.vehicleservices.api;

import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.FileUploadService;
import com.automate.vehicleservices.service.MdVehicleDocumentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InterruptedIOException;

/**
 * @author Chandrashekar V
 */
@Tag(name = "File Upload And Download", description = "File Upload and Download API")
@RequestMapping(AbstractBaseController.BASE_URL + "/file-io")
@RestController
public class FileUploadDownloadController extends AbstractBaseController {


    private final FileUploadService fileUploadService;
    private final MdVehicleDocumentTypeService mdVehicleDocumentTypeService;
    private final Logger logger = LoggerFactory.getLogger(FileUploadDownloadController.class);

    public FileUploadDownloadController(FileUploadService fileUploadService,
                                        MdVehicleDocumentTypeService mdVehicleDocumentTypeService) {
        this.fileUploadService = fileUploadService;
        this.mdVehicleDocumentTypeService = mdVehicleDocumentTypeService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Upload a file. ", description = "upload a file", tags = {"File Upload"}, parameters =
            {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    public ResponseEntity<APIResponse<?>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("label") String label) {
        try {
            return responseEntity(fileUploadService.upload(file, label));
        } catch (InterruptedIOException e) {
            logger.error("Exception while uploading the file.");
        }
        throw new VehicleServicesException("upload failed.");
    }

    @GetMapping("/labels")
    @Operation(summary = "Document Types master data", description = "Document labels provided by the vendor. These " +
            "labels are the ones referenced when Images/Documents are uploaded.",
            tags = {"Body Repair Estimate"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    public ResponseEntity<APIResponse<?>> imageLabels(@PathVariable(TENANT) final String tenant) {
        return responseEntity(mdVehicleDocumentTypeService.all(getTenantId()));
    }
}
