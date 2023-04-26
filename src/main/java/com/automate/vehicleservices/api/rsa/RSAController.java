package com.automate.vehicleservices.api.rsa;

import com.automate.vehicleservices.api.model.rsa.RSARequest;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.rsa.RsaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;


import static com.automate.vehicleservices.common.VehicleServiceConstants.RSA;
import static com.automate.vehicleservices.common.VehicleServiceConstants.RSA_LIST;
import static com.automate.vehicleservices.common.VehicleServiceConstants.SORT_BY_CREATED_DATE;
import static com.automate.vehicleservices.common.VehicleServiceConstants.EMPTY_STRING;
import static com.automate.vehicleservices.common.VehicleServiceConstants.PER_PAGE_RECORDS;
import static com.automate.vehicleservices.common.VehicleServiceConstants.PAGE_ZERO;
import static com.automate.vehicleservices.common.VehicleServiceConstants.TECHNICIAN_LIST;

@RestController
@Tag(name = "RSA", description = "API to get RSA information")
@RequestMapping(/*value = AbstractBaseController.BASE_URL +*/ RSA)
public class RSAController extends AbstractBaseController {

    private final RsaService rsaService;
    private final Logger logger = LoggerFactory.getLogger(RSAController.class);

    public RSAController(RsaService rsaService) {
        this.rsaService = rsaService;
    }

    @Operation(summary = "Save RSA information")
    @PostMapping
    public ResponseEntity<APIResponse<?>> saveRsa(@RequestBody RSARequest rsaRequest) {
        return responseEntity(rsaService.saveRSA(rsaRequest));
    }

    @Operation(summary = "update RSA information")
    @PutMapping({"/id/{id}"})
    public ResponseEntity<APIResponse<?>> updateRsa(@PathVariable("id") int id, @RequestBody RSARequest rsaRequest) {
        return responseEntity(rsaService.updateRSA(id, rsaRequest));
    }

    @Operation(summary = "Get All RSA")
    @GetMapping(value = "/all", produces = "application/json")
    @Transactional
    public ResponseEntity<APIResponse<?>> getAllPerformance(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestParam(value = "customerId", required = false) Integer customerId,
            @RequestParam(value = "customerid", required = false) Integer customerid,
            @RequestParam(value = "status", required = false) String status) {
    	System.out.println("<<<<<<<<<<<<<<<<<<<<<customerid>>>>>>>>>>>>>>>>>>>>>>>>>>>"+customerid);
    	return responseEntity(rsaService.getAllRsa(page, size, customerid, status));
    }

    @Operation(summary = "Get RSA By Customer ID")
    @GetMapping(value = "/customer_id/{customerId}", produces = "application/json")
    @Transactional
    public ResponseEntity<APIResponse<?>> getRsaByCustomerId(
            @PathVariable(name = "customerId") Integer customerId) {
        return responseEntity(rsaService.fetchRSAByCustomerId(customerId));
    }

    @Operation(summary = "Get RSA By ID")
    @GetMapping(value = "/id/{id}", produces = "application/json")
    @Transactional
    public ResponseEntity<APIResponse<?>> getRsa(
            @PathVariable(name = "id") int id) {
        return responseEntity(rsaService.fetchRSAById(id));
    }

    @Operation(summary = "Delete RSA By ID")
    @DeleteMapping({"/id/{id}"})
    public ResponseEntity<APIResponse<?>> deleteRsaById(
            @PathVariable("id") Integer id) {
        rsaService.deleteRsaById(id);
        return ResponseEntity.ok(defaultVoidResponse(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = RSA_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRSAList(@RequestParam(value = "page", required = false, defaultValue = PAGE_ZERO) Integer page,
                                        @RequestParam(value = "size", required = false, defaultValue = PER_PAGE_RECORDS) Integer size,
                                        @RequestParam(value = "sortBy", required = false, defaultValue = SORT_BY_CREATED_DATE) String sortBy,
                                        @RequestParam(value = "searchBy", required = false, defaultValue = EMPTY_STRING) String searchBy){
        return responseEntity(rsaService.getRSAList(page, size, sortBy, searchBy));
    }

    @GetMapping(value = TECHNICIAN_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTechnicianList(){
        return responseEntity(rsaService.getTechnicianDetailsDto());
    }
}