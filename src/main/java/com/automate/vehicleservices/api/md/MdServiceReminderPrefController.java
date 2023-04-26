package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;
import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.service.MdServiceReminderPrefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = AbstractCRUDController.MD_BASE_URL + "/service-reminder-pref")
@Tag(name = "Master data - Service Reminder Preferences API", description = "Service Reminder Preferences API")
@RestController
@Slf4j
public class MdServiceReminderPrefController extends AbstractCRUDController {

    private final MdServiceReminderPrefService mdServiceReminderPrefService;

    public MdServiceReminderPrefController(MdServiceReminderPrefService mdServiceReminderPrefService) {
        this.mdServiceReminderPrefService = mdServiceReminderPrefService;
    }

    @Override
    public MasterDataService getService() {
        return mdServiceReminderPrefService;
    }


    @Operation(tags = {"Master data - Service Reminder Preferences API"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewServiceReminderPreference(
            @Validated @RequestBody MdServiceReminderPrefRequest mdRequest) {
        return responseEntity(getService().save(mdRequest, getTenantId()));
    }

    @Operation(summary = "Returns supported time frames",
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/time-frame")
    public ResponseEntity<APIResponse<?>> timeFrames() {
        return responseEntity(TimeFrame.values());

    }

    @Operation(summary = "Returns supported Expressions",
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/expressions")
    public ResponseEntity<APIResponse<?>> expressions() {
        return responseEntity(Expression.values());

    }

}
