package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceItemService;
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

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-item")
@Tag(name = "Master data - Service Item API", description = "Service Item API")
@RestController
@Slf4j
public class MdServiceItemController extends AbstractCRUDController {

    private final MdServiceItemService mdServiceItemService;

    public MdServiceItemController(MdServiceItemService mdServiceItemService) {
        this.mdServiceItemService = mdServiceItemService;
    }

    @Override
    public MasterDataService getService() {
        return mdServiceItemService;
    }

    @Operation(tags = {"Master data - Service Item API"}, parameters = {@Parameter(in = ParameterIn.PATH, required =
            true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewServiceItem(
            @Validated @RequestBody MdServiceItemRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }
}
