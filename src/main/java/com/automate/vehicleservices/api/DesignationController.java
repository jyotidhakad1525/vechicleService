package com.automate.vehicleservices.api;


import com.automate.vehicleservices.api.model.DesignationRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.DesignationService;
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

@Tag(name = "Designation", description = "Designation API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/designation")
@Validated
public class DesignationController extends AbstractBaseController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @Operation(summary = "API to add new Designation and update existing Designation", description = "Add new Designation into the system.", tags = {"Designation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/addOrUpdate")
    public ResponseEntity<APIResponse<?>> addNewDesignation(@Valid @RequestBody DesignationRequest designationRequest) {
        return responseEntity(designationService.saveAndUpdateDepartment(designationRequest, getOrgBasedOnTenantId()));

    }

    @Operation(summary = "API to fetch list of Designation in the order of creation date", description = "List of Designation", tags = {"Designation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " + "index)")
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " + "10")})
    @GetMapping("/fetch-designation")
    public ResponseEntity<APIResponse<?>> fetchAllDesignation(Pagination pagination) {
        return responseEntity(designationService.fetchAllDesignation(pagination, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to fetch Designation based on designation id", description = "Fetch Designation based on id", tags = {"Designation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/fetch-designation/{id}")
    public ResponseEntity<APIResponse<?>> fetchDesignationBasedOnId(@PathVariable Integer id) {
        return responseEntity(designationService.fetchDesignationBasedOnId(id, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to delete Designation based on designation id", description = "Delete Designation based on id", tags = {"Designation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/delete-designation/{id}")
    public ResponseEntity<APIResponse<?>> deleteDesignationBasedOnId(@PathVariable Integer id) {
        designationService.deleteDesignationBasedOnId(id, getOrgBasedOnTenantId().getId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to Search for Designation", description = "Search for designation", tags = {"Designation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "designationName")
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "designationValue")
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "status")
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "departmentId")
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " + "index)")
            , @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " + "10")})
    @GetMapping("/search")
    public ResponseEntity<APIResponse<?>> search(DesignationRequest designationRequest, Pagination pagination) {
        return responseEntity(designationService.search(designationRequest, pagination, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to fetch Designation based on department id", description = "Fetch Designation based on department id", tags = {"Designation"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "departmentId")})
    @GetMapping("/fetch-designation-by-department/{departmentId}")
    public ResponseEntity<APIResponse<?>> fetchDesignationBasedOnDepartmentId(@PathVariable Integer departmentId) {
        return responseEntity(designationService.fetchDesignationBasedOnDepartmentId(departmentId, getOrgBasedOnTenantId().getId()));
    }
}
