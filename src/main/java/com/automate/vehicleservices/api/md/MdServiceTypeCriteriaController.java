package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceTypeCriteriaService;
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

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-type-criteria")
@Tag(name = "Master data - Service Type Criteria API", description = "Service Type Criteria API")
@RestController
@Slf4j
public class MdServiceTypeCriteriaController extends AbstractCRUDController {

    private final MdServiceTypeCriteriaService mdServiceTypeCriteriaService;

    public MdServiceTypeCriteriaController(MdServiceTypeCriteriaService mdServiceTypeCriteriaService) {
        this.mdServiceTypeCriteriaService = mdServiceTypeCriteriaService;
    }

    @Override
    public MasterDataService getService() {
        return mdServiceTypeCriteriaService;
    }

    @Operation(tags = {"Master data - Service Type API"}, parameters = {@Parameter(in = ParameterIn.PATH, required =
            true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewServiceTypeCriteria(
            @Validated @RequestBody MdServiceTypeCriteriaRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }

}
