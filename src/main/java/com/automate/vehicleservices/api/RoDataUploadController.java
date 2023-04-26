package com.automate.vehicleservices.api;


import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.RoDataUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "RO Data Upload", description = "RO Data Upload API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/roData")
@Validated
public class RoDataUploadController extends AbstractBaseController {

    private final RoDataUploadService roDataUploadService;

    public RoDataUploadController(RoDataUploadService roDataUploadService) {
        this.roDataUploadService = roDataUploadService;
    }


    @Operation(summary = "API to upload ro data",
            description = "API to upload ro data",
            tags = {"RO Data"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<APIResponse<?>> uploadRoData(@RequestParam("file") MultipartFile file) {
        roDataUploadService.uploadRoData(file, getTenantId(),getTenant());
        return responseEntity(new APIResponse(HttpStatus.OK));

    }


}
