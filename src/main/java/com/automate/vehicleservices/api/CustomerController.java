package com.automate.vehicleservices.api;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.automate.vehicleservices.api.model.BulkUploadResponse;
import com.automate.vehicleservices.api.model.CustomerSearchRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.api.model.UpdateCustomerRequest;
import com.automate.vehicleservices.api.model.v1.CustomerRequestV1;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.CustomerService;
import com.automate.vehicleservices.service.facade.CustomerFacade;
import com.automate.vehicleservices.service.facade.CustomerSearchFacade;
import com.automate.vehicleservices.service.facade.v1.CustomerFacadeV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Customer", description = "Customer API")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/customer")
public class CustomerController extends AbstractBaseController {

    private final CustomerFacade customerFacade;

    private final CustomerSearchFacade customerSearchFacade;

    private final CustomerService customerService;

    private final CustomerFacadeV1 customerFacadeV1;

    public CustomerController(CustomerFacade customerFacade, CustomerService customerService,
                              CustomerSearchFacade customerSearchFacade, CustomerService customerService1, CustomerFacadeV1 customerFacadeV1) {
        this.customerFacade = customerFacade;
        this.customerSearchFacade = customerSearchFacade;
        this.customerService = customerService1;
        this.customerFacadeV1 = customerFacadeV1;
    }


    @Operation(summary = "API to add new Customer",
            description = "Add new Customer into the system. Tenant information is mandatory. If customer already " +
                    "exists throws exception",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PostMapping
    public ResponseEntity<APIResponse<?>> addNewCustomer(
            @Validated @RequestBody CustomerRequestV1 customerRequest) {

        return responseEntity(customerFacadeV1.saveCustomer(customerRequest, getTenant()));

    }

    @Operation(summary = "API to update Customer",
            description = "Update customer. Contact number is used to identify existing customer to update. If " +
                    "Customer not  exists throws exception",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @PutMapping("/{customer-id}")
    public ResponseEntity<APIResponse<?>> updateCustomer(
            @Validated @RequestBody UpdateCustomerRequest customerRequest,
            @PathVariable("customer-id") int customerId) {
        return responseEntity(customerFacade.updateCustomer(customerId, customerRequest, getTenant()));

    }

    @Operation(summary = "API to check whether contact number is already mapped to another Customer",
            description = "Returns true if contact number is already in use.",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.QUERY, required = true, name =
            "contactNumber"), @Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/contact-number-check")
    public ResponseEntity<APIResponse<?>> contactNumberCheck(
            @RequestParam(name = "contactNumber", required = true) @Valid @NotBlank final String contactNumber) {
        if (StringUtils.isBlank(contactNumber))
            throw new VehicleServicesException("Contact number can not be blank.");
        return responseEntity(customerService.isContactNumberAlreadyMapped(contactNumber));

    }


    @Operation(summary = "API to fetch customer by  contact number.",
            description = "Fetch customer by contact number",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            "contact-number"), @Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/by-contact-number/{contact-number}")
    public ResponseEntity<APIResponse<?>> customerByContactNumber(
            @PathVariable(name = "contact-number", required = true) @Valid @NotBlank final String contactNumber) {
        if (StringUtils.isBlank(contactNumber))
            throw new VehicleServicesException("Contact number can not be blank.");
        return responseEntity(customerService.fetchCustomerDTOByContactNumber(contactNumber));

    }

    @Operation(summary = "API to fetch customer by  Id.",
            description = "Fetch customer by Customer ID",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = "id"),
            @Parameter(in = ParameterIn.PATH, required = true, name = TENANT)})
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<?>> customerById(
            @PathVariable(name = "id", required = true) final int id) {
        return responseEntity(customerService.findById(id));
    }

    @Operation(summary = "API to check whether contact number is already mapped to another Customer",
            description = "Returns true if contact number is already in use.",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.PATH, required = true, name = "id")})
    @GetMapping("/{id}/vehicles")
    public ResponseEntity<APIResponse<?>> vehicles(
            @PathVariable(name = "id", required = true) @Valid @NotBlank final int id) {
        return responseEntity(customerService.fetchVehiclesByCustomer(id));

    }

    @Operation(summary = "API to Search for Customers",
            description = "Search for customer",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "vehicleRegNumber"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "contactNumber"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "vin"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "chassis"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "customerName"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "customerId"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "engineNumber"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "policyNumber"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "jobCardNumber"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/search")
    public ResponseEntity<APIResponse<?>> search(CustomerSearchRequest customerSearchRequest, Pagination pagination) {
        return responseEntity(customerSearchFacade.search(customerSearchRequest, getTenant(), pagination.getPage(),
                pagination.getSize()));

    }

    @Operation(summary = "API to list recent Customers in the order of creation date",
            description = "List recent customers",
            tags = {"Customer"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th " +
                    "index)"),
            @Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is " +
                    "10")})
    @GetMapping("/recent-customers")
    public ResponseEntity<APIResponse<?>> allRecentCustomersByTenant(Pagination pagination) {
        return responseEntity(customerSearchFacade.list(getTenant(), pagination.getPage(), pagination.getSize()));
    }

	@Operation(summary = "API to add new Customer", description = "Add new Customer into the system. Tenant information is mandatory. If customer already "
			+ "exists throws exception", tags = {
					"Customer" }, parameters = { @Parameter(in = ParameterIn.PATH, required = true, name = TENANT) })
	@PostMapping(value = "/customerBulkUpload")
	public ResponseEntity<APIResponse<?>> createAutBulkUpload2(HttpServletRequest request,
			@RequestParam("file") MultipartFile bulkExcel) throws IOException {
		try {
			BulkUploadResponse bulkUploadResponse = customerFacade.processBulkExcelForCustomerUpload(bulkExcel,
					getTenant());
			return responseEntity(bulkUploadResponse);
		} catch (Exception e) {
			BulkUploadResponse res = new BulkUploadResponse();
			List<String> FailedRecords = new ArrayList<>();
			String resonForFailure = e.getMessage();
			FailedRecords.add(resonForFailure);
			res.setFailedCount(0);
			res.setFailedRecords(FailedRecords);
			res.setSuccessCount(0);
			res.setTotalCount(0);
			return responseEntity(res);
		}
	}


}
