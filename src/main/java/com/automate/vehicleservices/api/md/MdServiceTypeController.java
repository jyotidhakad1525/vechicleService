package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-type")
@Tag(name = "Master data - Service Type API", description = "Service Type API")
@RestController
@Slf4j
public class MdServiceTypeController extends AbstractCRUDController {

    private final MdServiceTypeService serviceTypeService;

    public MdServiceTypeController(MdServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @Override
    public MasterDataService getService() {
        return serviceTypeService;
    }

    @Operation(tags = {"Master data - Service Type API"}, parameters = {@Parameter(in = ParameterIn.PATH, required =
            true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewServiceType(
            @Validated @RequestBody MdServiceTypeRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }

    @Operation(tags = {"Master data - Service Type API"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PatchMapping("/{id}/service-item")
    public ResponseEntity<APIResponse<?>> mapServiceItemToServiceType(
            @Validated @RequestBody MdServiceItemMappingRequest mdRequest, @PathVariable int id) {
        return responseEntity(serviceTypeService.mapServiceItemToServiceType(id, mdRequest, getTenantId()));

    }

    @Operation(tags = {"Master data - Service Type API"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @DeleteMapping(name = "delete-service-item-mapping", value = "/{id}/service-item")
    public ResponseEntity<APIResponse<?>> deleteServiceItemToServiceTypeMapping(
            @Validated @RequestBody MdServiceItemMappingRequest mdRequest, @PathVariable int id) {
        return responseEntity(serviceTypeService.deleteServiceItemToServiceTypeMapping(id, mdRequest, getTenantId()));
    }

}
