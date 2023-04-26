package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-slot")
@Tag(name = "Master data - Service Slot API", description = "Service Slot API")
@RestController
@Slf4j
public class MdServiceSlotController extends AbstractCRUDController {

    private final MdServiceSlotService mdServiceSlotService;

    public MdServiceSlotController(MdServiceSlotService mdServiceSlotService) {
        this.mdServiceSlotService = mdServiceSlotService;
    }

    @Override
    public MasterDataService getService() {
        return mdServiceSlotService;
    }

    @Operation(tags = {"Master data - Service slot API"}, parameters = {@Parameter(in = ParameterIn.PATH, required =
            true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewSlot(
            @Validated @RequestBody MdServiceSlotRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));

    }

    @Operation(tags = {"Master data - Service slot API"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<?>> updateSlot(
            @Validated @RequestBody MdServiceSlotRequest mdRequest, @PathVariable("id") int id) {
        return responseEntity(mdServiceSlotService.update(mdRequest, getTenantId(), id));

    }

    @Operation(tags = {"Master data - Service slot API"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/all-by-day")
    public ResponseEntity<APIResponse<?>> allByDay() {
        return responseEntity(mdServiceSlotService.slotsByDay(getTenantId()));

    }
}
