package com.automate.vehicleservices.api;

import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.MdTenantService;
import com.automate.vehicleservices.service.ServiceAppointmentService;
import com.automate.vehicleservices.service.ServiceEstimateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Chandrashekar V
 */
@Tag(name = "Organization", description = "Organization API")
@RestController
@RequestMapping("/api/org")
public class OrganizationController extends AbstractBaseController {


    private final MdTenantService mdTenantService;
    private final ServiceAppointmentService appointmentService;
    private final ServiceEstimateService serviceEstimateService;

    public OrganizationController(MdTenantService mdTenantService,
                                  ServiceAppointmentService appointmentService,
                                  ServiceEstimateService serviceEstimateService) {
        this.mdTenantService = mdTenantService;
        this.appointmentService = appointmentService;
        this.serviceEstimateService = serviceEstimateService;
    }

    @Operation(summary = "List of cities where Organization service center exist. ", description = "Given the Org " +
            "identifier, return list of cities", tags = {"Organization"}, parameters = {@Parameter(in =
            ParameterIn.PATH, required = true, name = "org_identifier")})
    @GetMapping("/{org_identifier}/cities")
    public ResponseEntity<APIResponse<?>> listOfTenantCities() {
        return responseEntity(mdTenantService.listOfCitiesByTheOrganization(getOrg()));
    }

    @Operation(summary = "List of service centers for the given Organization and City. ", description =
            "Given the Org identifier and City, return list of Service Centers", tags = {"Organization"}, parameters
            = {@Parameter(in = ParameterIn.PATH, required = true, name = "org_identifier")})
    @GetMapping("/{org_identifier}/service-centers")
    public ResponseEntity<APIResponse<?>> listOfServiceCenters(@RequestParam("city") String city) {
        return responseEntity(mdTenantService.listOfTenantsByTheCityAndOrganization(city, getOrg()));
    }

    @Operation(summary = "Fetch all Appointments by customer and Organization", description = "Returns active " +
            "appointments for today and later for the given Organization ",
            tags = {"Appointment"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            "org_identifier"),
            @Parameter(in = ParameterIn.PATH, required = true, name =
                    "customer-contact-number")
            ,@Parameter(in = ParameterIn.QUERY, required = false, name =
            "slotId")})
    @GetMapping("/{org_identifier}/customer/{customer-contact-number}/appointments")
    public ResponseEntity<APIResponse<?>> fetchAppointmentsByOrgAndCustomer(
            @RequestParam(required = false) Integer slotId,
            @PathVariable("org_identifier") final String tenant,
            @PathVariable("customer-contact-number") final String customerContactNumber, Pagination pagination) {

        return responseEntity(appointmentService.fetchAppointmentsByOrgAndCustomer(getOrg(), customerContactNumber,
                pagination,slotId));
    }

    @Operation(summary = "Fetch service group data by customer and Organization", description = "Returns required " +
            "service group data for the given customer,  Organization and service group.",
            tags = {"Appointment"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            "org_identifier"),
            @Parameter(in = ParameterIn.PATH, required = true, name =
                    "customer-contact-number")})
    @GetMapping("/{org_identifier}/customer/{customer-contact-number}/{service-group}")
    public ResponseEntity<APIResponse<?>> fetchEstimatesByOrgAndCustomer(
            @PathVariable("org_identifier") final String tenant,
            @PathVariable("customer-contact-number") final String customerContactNumber,
            @PathVariable("service-group") final ServiceGroup serviceGroup) {

        return responseEntity(serviceEstimateService.fetchEstimatesByOrgAndCustomerAndServiceGroup(getOrg(),
                customerContactNumber, serviceGroup));
    }

}
