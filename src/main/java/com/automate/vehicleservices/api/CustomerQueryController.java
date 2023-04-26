package com.automate.vehicleservices.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.customerquery.CustomerQueryRequest;
import com.automate.vehicleservices.api.model.customerquery.CustomerQuerySearchRequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.notification.NotificationStrategy;
import com.automate.vehicleservices.notification.SMSNotificationData;
import com.automate.vehicleservices.service.CustomerQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Customer Query", description = "Customer Query API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/customer-query")
public class CustomerQueryController extends AbstractBaseController {

    private final CustomerQueryService customerQueryService;


    public CustomerQueryController(CustomerQueryService customerQueryService) {
        this.customerQueryService = customerQueryService;
    }

    @PostMapping
    @Operation(summary = "Submit Customer Query", description = "Submit Customer Query details")
    public ResponseEntity<APIResponse<?>> submitCustomerQuery(@Valid @RequestBody CustomerQueryRequest customerQueryRequest) {
    	System.out.println("<<<<<<<<<<<<<CustomerID In Controller>>>>>>>>>>>"+customerQueryRequest.getCustomerId());
    	System.out.println("<<<<<<<<<<<<<assignedTo In Controller>>>>>>>>>>>"+customerQueryRequest.getAssignedTo());
    	System.out.println("<<<<<<<<<<<<<crmRemarks In Controller>>>>>>>>>>>"+customerQueryRequest.getCrmRemarks());
    	System.out.println("<<<<<<<<<<<<<query In Controller>>>>>>>>>>>"+customerQueryRequest.getQuery());

    	return responseEntity(customerQueryService.createCustomerQueryBilla(customerQueryRequest));

    }

    @PutMapping("/id/{id}")
    @Operation(summary = "Update Customer Query", description = "Update Customer Query details")
    public ResponseEntity<APIResponse<?>> updateCustomerQuery(@PathVariable("id") int id,
                                                              @Valid @RequestBody CustomerQueryRequest customerQueryRequest) {
        return responseEntity(customerQueryService.updateCustomerQuery(id, customerQueryRequest));

    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Delete Customer Query", description = "Delete Customer Query details")
    public ResponseEntity<APIResponse<?>> deleteCustomerQuery(@PathVariable(name = "id", required = true) Integer id) {
        customerQueryService.deleteCustomerQuery(id);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));

    }

    @Operation(summary = "API to Search for customer queries",
            description = "Search for customer queries",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "id"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "assignedTo"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "createdBy"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/search")
    public ResponseEntity<APIResponse<?>> search(CustomerQuerySearchRequest customerSearchRequest,
                                                 Pagination pagination) {
        return responseEntity(customerQueryService.searchCustomerQueriesBilla(customerSearchRequest, getTenant(),
                pagination.getPage(), pagination.getSize()));

    }

}
