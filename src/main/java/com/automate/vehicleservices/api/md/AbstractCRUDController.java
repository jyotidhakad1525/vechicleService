package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.MasterDataService;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class AbstractCRUDController extends AbstractBaseController {
    protected static final String MD_BASE_URL = BASE_URL + "/master-data";

    public abstract MasterDataService getService();

    @Operation(parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<?>> deactivate(
            @PathVariable("id") int id) {
        return responseEntity(getService().delete(id, getTenantId()));

    }

    @Operation(parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/all")
    public ResponseEntity<APIResponse<?>> all() {
        return responseEntity(getService().all(getTenantId()));

    }


    @Operation(parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<?>> findByIdAndTenant(@PathVariable("id") int id) {
        return responseEntity(getService().findById(id, getTenantId()));

    }
}
