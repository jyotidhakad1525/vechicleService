package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdMaintenanceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/maintenance-type")
@Tag(name = "Master data - Maintenance Type API", description = "Maintenance Type APIs")
@RestController
@Slf4j
public class MdMaintenanceTypeController extends AbstractCRUDController {

    private final MdMaintenanceTypeService mdMaintenanceTypeService;

    public MdMaintenanceTypeController(MdMaintenanceTypeService mdMaintenanceTypeService) {
        this.mdMaintenanceTypeService = mdMaintenanceTypeService;
    }

    @Operation(tags = {"Master data - Maintenance Type API"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewMaintenanceType(
            @Validated @RequestBody MdMaintenanceTypeRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }

    @Override
    public MasterDataService getService() {
        return mdMaintenanceTypeService;
    }
}
