package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.md.MdLeadSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/lead-source")
@Tag(name = "Master data - Lead Source API", description = "Lead Source API")
@RestController
@Slf4j
public class LeadSourceController extends AbstractCRUDController {

    private final MdLeadSourceService mdLeadSourceService;

    public LeadSourceController(MdLeadSourceService mdLeadSourceService) {
        this.mdLeadSourceService = mdLeadSourceService;
    }

    @Override
    public MasterDataService getService() {
        return mdLeadSourceService;
    }

    @Operation(parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/all-by-hierarchy")
    public ResponseEntity<APIResponse<?>> allByHierarchy() {
        return responseEntity(mdLeadSourceService.allByHierarchy(getTenantId()));

    }
}
