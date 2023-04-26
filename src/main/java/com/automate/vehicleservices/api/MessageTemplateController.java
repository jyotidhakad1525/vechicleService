package com.automate.vehicleservices.api;


import com.automate.vehicleservices.api.model.MessageTemplateRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.MessageTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Template", description = "Template API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/template")
@Validated
public class MessageTemplateController extends AbstractBaseController {

    private final MessageTemplateService messageTemplateService;

    public MessageTemplateController(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }


    @Operation(summary = "API to add new Template and update existing Template",
            description = "Add new Template into the system.",
            tags = {"Template"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/addOrUpdate")
    public ResponseEntity<APIResponse<?>> addNewTemplate(
            @Valid @RequestBody MessageTemplateRequest messageTemplateRequest) {
        return responseEntity(messageTemplateService.saveAndUpdateTemplate(messageTemplateRequest, getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to fetch list of Template in the order of creation date",
            description = "List of Template",
            tags = {"Template"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "type"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/fetch-template-all/{type}")
    public ResponseEntity<APIResponse<?>> fetchAllTemplateBasedOnTemplateType(Pagination pagination, @PathVariable int type) {
        return responseEntity(messageTemplateService.fetchAllTemplateBasedOnTemplateType(type, pagination, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to fetch Template based on template id",
            description = "Fetch Template based on id",
            tags = {"Template"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/fetch-template/{id}")
    public ResponseEntity<APIResponse<?>> fetchTemplateBasedOnId(@PathVariable Integer id) {
        return responseEntity(messageTemplateService.fetchTemplateBasedOnId(id, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to delete Template based on template id",
            description = "Delete Template based on id",
            tags = {"Template"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id"),})
    @DeleteMapping("/delete-template/{id}")
    public ResponseEntity<APIResponse<?>> deleteTemplateBasedOnId(@PathVariable Integer id) {
        messageTemplateService.deleteTemplateBasedOnId(id, getOrgBasedOnTenantId().getId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to Search for Template",
            description = "Search for template",
            tags = {"Template"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "templateName"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "template"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "status"),
            @Parameter(in = ParameterIn.PATH, required = true, name = "type"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/search/{type}")
    public ResponseEntity<APIResponse<?>> search(@PathVariable int type, MessageTemplateRequest messageTemplateRequest, Pagination pagination) {
        return responseEntity(messageTemplateService.search(type, messageTemplateRequest, pagination, getOrgBasedOnTenantId().getId()));
    }
}
