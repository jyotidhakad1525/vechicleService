package com.automate.vehicleservices.api;


import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employee", description = "Employee API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/employee")
public class EmployeeController extends AbstractBaseController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "API to fetch employees by role",
            description = "API to fetch employees by role",
            tags = {"Employee"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/all/by-role")
    public ResponseEntity<APIResponse<?>> listByRole() {
        return responseEntity(employeeService.listAllEmployeesByTenant(getTenantId()));
    }

    @Operation(summary = "API to fetch cre based on org",
            description = "API to fetch cre based on org",
            tags = {"Employee"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/all/cre")
    public ResponseEntity<APIResponse<?>> getAllCreBasedOnOrg() {
        return responseEntity(employeeService.getAllCreBasedOnOrg(getOrgBasedOnTenantId()));
    }


}
