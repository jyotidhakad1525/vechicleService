package com.automate.vehicleservices.api;

import com.automate.vehicleservices.api.model.FollowUpMasterDataType;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.followup.FollowUpRequest;
import com.automate.vehicleservices.api.model.followup.FollowUpUpdateRequest;
import com.automate.vehicleservices.entity.enums.FollowUpCategory;
import com.automate.vehicleservices.entity.enums.FollowUpReason;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.ServiceReminderFollowUpActivityService;
import com.automate.vehicleservices.service.ServiceReminderFollowUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Tag(name = "Service Reminder - Follow-Up", description = "Follow-Up API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/followup")
public class ServiceFollowUpController extends AbstractBaseController {

    private final ServiceReminderFollowUpService serviceReminderFollowUpService;
    private final ServiceReminderFollowUpActivityService serviceReminderFollowUpActivityService;

    public ServiceFollowUpController(
            ServiceReminderFollowUpService serviceReminderFollowUpService,
            ServiceReminderFollowUpActivityService serviceReminderFollowUpActivityService) {
        this.serviceReminderFollowUpService = serviceReminderFollowUpService;
        this.serviceReminderFollowUpActivityService = serviceReminderFollowUpActivityService;
    }

    @Operation(summary = "Create followup activity ", description = "Update followup activity ",
            tags = {"Service Reminder - Follow-Up"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true,
            name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> createFollowUp(@Valid @RequestBody FollowUpRequest followUpRequest) {
        return responseEntity(serviceReminderFollowUpService.createFollowupActivity(followUpRequest));
    }

    @Operation(summary = "Update followup activity details", description = "Update followup activity details",
            tags = {"Service Reminder - Follow-Up"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true,
            name = TENANT)})
    @PutMapping
    public ResponseEntity<APIResponse<?>> updateFollowUpActivity(
            @Valid @RequestBody FollowUpUpdateRequest followUpUpdateRequest) {
        return responseEntity(serviceReminderFollowUpService.updateFollowupActivity(followUpUpdateRequest,
                getLoggedInEmpId()));
    }

    @Operation(summary = "Follow up master data", description = "Follow-up activity master data",
            tags = {"Service Reminder - Follow-Up"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true,
            name = TENANT), @Parameter(in = ParameterIn.PATH, required = true, name = "type")
    })
    @GetMapping("/master-data/{type}")
    public ResponseEntity<APIResponse<?>> masterData(
            @PathVariable("type") @Valid @NotBlank FollowUpMasterDataType type) {
        return responseEntity(serviceReminderFollowUpService.masterData(type));
    }

    // API to give stats + API to send calls by logged in user  + status
    @Operation(summary = "List of Follow ups by Vehicle", description = "Follow-ups by Vehicle ",
            tags = {"Service Reminder - Follow-Up entries"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
                    @Parameter(in = ParameterIn.PATH, required = true, name = "reg-number")
            })
    @GetMapping("/vehicle/{reg-number}")
    public ResponseEntity<APIResponse<?>> activeFollowUpsByVehicle(
            @PathVariable("reg-number") @Valid @NotBlank String regNumber) {

        return responseEntity(serviceReminderFollowUpService.activeFollowUpsByVehicle(regNumber));
    }

    @Operation(summary = "List of Follow ups by tenant", description = "All active Follow-ups by Tenant ",
            tags = {"Service Reminder - Follow-Up entries"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            })
    @GetMapping
    public ResponseEntity<APIResponse<?>> activeFollowUpsByTenant() {
        return responseEntity(serviceReminderFollowUpService.allActiveFollowUpsByTenant(getTenant()));
    }

    @Operation(summary = "List of Follow ups by logged in user", description = "All active Follow-ups by Logged in " +
            "user ", tags = {"Service Reminder - Follow-Up"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
                    @Parameter(in = ParameterIn.PATH, required = true, name = "category"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. " +
                            "(0th index)"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page " +
                            "size is 10"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "serviceId", description = "serviceId"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "fromDate", description = "fromDate " +
                            "(yyyy-MM-dd)",
                            schema = @Schema(format = "yyyy-MM-dd")),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "toDate", description = "toDate " +
                            "(yyyy-MM-dd)",
                            schema = @Schema(format = "yyyy-MM-dd")),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "vehicleModel", description =
                            "vehicleModel")
            })
    @GetMapping("/category/{category}")
    public ResponseEntity<APIResponse<?>> activeFollowUpsByTenantAndUserId(
            @PathVariable("category") FollowUpCategory category, Pagination pagination,
            @RequestParam(value = "serviceId", required = false) Integer serviceId,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(name = "vehicleModel", required = false) String vehicleModel) {
        return responseEntity(
                serviceReminderFollowUpActivityService.followUpsByTenantAndLoggedInUserAndCategory(getTenant(),
                        getLoggedInEmpId(), category, pagination.getPage(), pagination.getSize(), serviceId, fromDate
                        , toDate, vehicleModel));
    }


    @Operation(summary = "List of Follow ups by logged in user", description = "All active Follow-ups by Logged in " +
            "user ", tags = {"Service Reminder - Follow-Up"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
                    @Parameter(in = ParameterIn.PATH, required = true, name = "categoryType"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. " +
                            "(0th index)"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page " +
                            "size is 10"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "serviceId", description = "serviceId"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "categoryId", description = "categoryId"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "location", description = "location"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "serviceCenterCode", description = "serviceCenterCode"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "fromDate", description = "fromDate " +
                            "(yyyy-MM-dd)",
                            schema = @Schema(format = "yyyy-MM-dd")),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "toDate", description = "toDate " +
                            "(yyyy-MM-dd)",
                            schema = @Schema(format = "yyyy-MM-dd")),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "vehicleModel", description =
                            "vehicleModel")
            })
    @GetMapping("/v1/category/{categoryType}")
    public ResponseEntity<APIResponse<?>> followUpDetails(
            @PathVariable("categoryType") FollowUpCategory categoryType, Pagination pagination,
            @RequestParam(value = "serviceId", required = false) Integer serviceId,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "serviceCenterCode", required = false) String serviceCenterCode,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(name = "vehicleModel", required = false) String vehicleModel) {
        return responseEntity(
                serviceReminderFollowUpActivityService.followUpDetails(getTenant(),
                        getLoggedInEmpId(), categoryType, pagination.getPage(), pagination.getSize(), serviceId, fromDate
                        , toDate, vehicleModel,getOrgBasedOnTenantId(),categoryId,location,serviceCenterCode));
    }

    @Operation(summary = "List of Follow ups by logged in user", description = "All active Follow-ups by Logged in " +
            "user ", tags = {"Service Reminder - Follow-Up"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/v1/stats")
    public ResponseEntity<APIResponse<?>> statsV1() {
        return responseEntity(
                serviceReminderFollowUpActivityService.followUpStatsV1( getLoggedInEmpId(),getOrgBasedOnTenantId()));
    }

    @Operation(summary = "List of Follow ups by logged in user", description = "All active Follow-ups by Logged in " +
            "user ", tags = {"Service Reminder - Follow-Up"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "dealerCode"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "location"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "from"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "to"),
                    @Parameter(in = ParameterIn.QUERY, required = false, name = "creId")})
    @GetMapping("/v1/dashboard")
    public ResponseEntity<APIResponse<?>> dashboardData(@RequestParam(required = false) String dealerCode,
                                                        @RequestParam(required = false) String location,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                        @RequestParam(required = false) Integer creId) {
        return responseEntity(
                serviceReminderFollowUpActivityService.dashboardData(dealerCode,location,from,to,creId, getLoggedInEmpId(),getOrgBasedOnTenantId()));
    }

    @Operation(summary = "List of Follow ups by logged in user", description = "All active Follow-ups by Logged in " +
            "user ", tags = {"Service Reminder - Follow-Up"},
            parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/stats")
    public ResponseEntity<APIResponse<?>> stats() {
        return responseEntity(
                serviceReminderFollowUpActivityService.followUpStats(getTenant(), getLoggedInEmpId()));
    }


    @Operation(summary = "List of Follow Activities by given Follow-up reason and tenant",
            description = "List of Follow Activities by given Follow-up  and Tenant ",
            tags = {"Service Reminder - Follow-Up"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
                    @Parameter(in = ParameterIn.PATH, required = true, name = "followup-reason")
            })
    @GetMapping("/activities/reason/{followup-reason}")
    public ResponseEntity<APIResponse<?>> followUpActivitiesByReason(
            @PathVariable("followup-reason") FollowUpReason followUpReason) {

        return responseEntity(serviceReminderFollowUpActivityService.followUpActivitiesByTenantReason(getTenant(),
                followUpReason));
    }

    @Operation(summary = "List of Follow ups by Activities for the given Followup id and Tenant", description = "All " +
            "activities for the given Follow-up id and by Tenant ",
            tags = {"Service Reminder - Follow-Up entries"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
                    @Parameter(in = ParameterIn.PATH, required = true, name = "followup-id")
            })
    @GetMapping("{followup-id}/activity")
    public ResponseEntity<APIResponse<?>> followUpActivitiesByParentFollowUpId(
            @PathVariable("followup-id") int id) {
        return responseEntity(serviceReminderFollowUpService.followUpActivitiesByParentFollowUpId(getTenant(), id));
    }

    @Operation(summary = "FollowUp Activity for the given  id, parent follow up and Tenant",
            description = "FollowUp Activity for the given id, Follow-up id and Tenant ",
            tags = {"Service Reminder - Follow-Up"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
                    @Parameter(in = ParameterIn.PATH, required = true, name = "followup-id")
            })
    @GetMapping("{followup-id}/activity/{activity-id}")
    public ResponseEntity<APIResponse<?>> followUpActivityByIdAndByParentFollowUpIdAndTenant(
            @PathVariable("followup-id") int followUpId, @PathVariable("activity-id") int activityId) {

        return responseEntity(serviceReminderFollowUpActivityService
                .followUpActivitiesByParentFollowUpIdAndActivityId(getTenant(), followUpId, activityId));
    }

}
