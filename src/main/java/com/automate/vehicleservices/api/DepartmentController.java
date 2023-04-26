package com.automate.vehicleservices.api;


import com.automate.vehicleservices.api.model.DepartmentRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.DepartmentService;
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

@Tag(name = "Department", description = "Department API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/department")
@Validated
public class DepartmentController extends AbstractBaseController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "API to add new Department and update existing Department",
            description = "Add new Department into the system.",
            tags = {"Department"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping("/addOrUpdate")
    public ResponseEntity<APIResponse<?>> addNewDepartment(
            @Valid @RequestBody DepartmentRequest departmentRequest) {
        return responseEntity(departmentService.saveAndUpdateDepartment(departmentRequest, getOrgBasedOnTenantId()));
    }

    @Operation(summary = "API to fetch list of Department in the order of creation date",
            description = "List of Department",
            tags = {"Department"}, parameters = {
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/fetch-department")
    public ResponseEntity<APIResponse<?>> fetchAllDepartment(Pagination pagination) {
        return responseEntity(departmentService.fetchAllDepartment(pagination, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to fetch Department based on department id",
            description = "Fetch Department based on id",
            tags = {"Department"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/fetch-department/{id}")
    public ResponseEntity<APIResponse<?>> fetchDepartmentBasedOnId(@PathVariable Integer id) {
        return responseEntity(departmentService.fetchDepartmentBasedOnId(id, getOrgBasedOnTenantId().getId()));
    }

    @Operation(summary = "API to delete Department based on department id",
            description = "Delete Department based on id",
            tags = {"Department"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)
            , @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/delete-department/{id}")
    public ResponseEntity<APIResponse<?>> deleteDepartmentBasedOnId(@PathVariable Integer id) {
        departmentService.deleteDepartmentBasedOnId(id, getOrgBasedOnTenantId().getId());
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "API to Search for Department",
            description = "Search for department",
            tags = {"Department"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "departmentName"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "departmentValue"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "status"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/search")
    public ResponseEntity<APIResponse<?>> search(DepartmentRequest departmentRequest, Pagination pagination) {
        return responseEntity(departmentService.search(departmentRequest, pagination, getOrgBasedOnTenantId().getId()));
    }
}
