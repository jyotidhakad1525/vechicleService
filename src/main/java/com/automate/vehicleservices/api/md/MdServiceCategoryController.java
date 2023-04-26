package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-category")
@Tag(name = "Master data - Service Category API", description = "Service Category Master Data APIs")
@RestController
@Slf4j
public class MdServiceCategoryController extends AbstractCRUDController {

    private final MdServiceCategoryService mdServiceCategoryService;

    public MdServiceCategoryController(MdServiceCategoryService mdServiceCategoryService) {
        this.mdServiceCategoryService = mdServiceCategoryService;
    }

    @Operation(tags = {"Master data - Service Category API"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewServiceCategory(
            @Validated @RequestBody MdServiceCategoryRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }

    @Operation(tags = {"Master data - Service Category API"}, summary = "List of available service groups",
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/service-groups")
    public ResponseEntity<APIResponse<?>> serviceGroups() {
        return responseEntity(ServiceGroup.values());

    }


    @Operation(tags = {"Master data - Service Types by category API"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "category")})
    @GetMapping("/{category}/service-types")
    public ResponseEntity<APIResponse<?>> byCategory(
            @PathVariable int category) {
        return responseEntity(mdServiceCategoryService.findServiceTypesByCategory(category, getTenantId()));
    }

    @Override
    public MasterDataService getService() {
        return mdServiceCategoryService;
    }
}
