package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceSchedulingConfigService;
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

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-schedule-config")
@Tag(name = "Master data - Service Scheduling Config API", description = "Service Scheduling Config APIs")
@RestController
@Slf4j
public class MdServiceSchedulingConfigController extends AbstractCRUDController {

    private final MdServiceSchedulingConfigService mdServiceSchedulingConfigService;

    public MdServiceSchedulingConfigController(MdServiceSchedulingConfigService mdServiceSchedulingConfigService) {
        this.mdServiceSchedulingConfigService = mdServiceSchedulingConfigService;
    }

    @Operation(tags = {"Master data - Service Scheduling Config API"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewMaintenanceType(
            @Validated @RequestBody MdServiceSchedulingConfigRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }

    @Override
    public MasterDataService getService() {
        return mdServiceSchedulingConfigService;
    }
}
