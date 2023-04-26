package com.automate.vehicleservices.api;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.automate.vehicleservices.api.model.AllocateLeadToCre;
import com.automate.vehicleservices.api.model.LeadAllocationFilterRequest;
import com.automate.vehicleservices.api.model.LeadTransferFilterrequest;
import com.automate.vehicleservices.api.model.LeadTransferRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.repository.MdServiceTypeRepository;
import com.automate.vehicleservices.repository.ServiceVehicleRepository;
import com.automate.vehicleservices.service.LeadAllocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Lead allocation", description = "Lead allocation")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/leadAllocation")
public class LeadAllocationController extends AbstractBaseController {

	private final LeadAllocationService leadAllocationService;

	private final Object lock1 = new Object();

	public LeadAllocationController(ServiceVehicleRepository serviceVehicleRepository,
			MdServiceTypeRepository mdServiceTypeRepository, LeadAllocationService leadAllocationService) {
		this.leadAllocationService = leadAllocationService;
	}

	@Operation(summary = "List of leads which is to be allocated by tenant", description = "All active Leads", tags = {
	"Service Reminder - Follow-Up entries" }, parameters = {
			@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
			@Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th "
					+ "index)"),
			@Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is "
					+ "10") })
	@GetMapping("/get-all-Lead")
	public ResponseEntity<APIResponse<?>> getAllLead(Pagination pagination) {
		return responseEntity(leadAllocationService.getAllLead(pagination));
	}

	@Operation(summary = "List of leads by filter", description = "All active Leads", tags = {
			"Service Reminder - Follow-Up entries" }, parameters = {
					@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "startDueDate", description = "startDueDate "
							+ "(yyyy-MM-dd)", schema = @Schema(format = "yyyy-MM-dd")),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "endDueDate", description = "endDueDate "
							+ "(yyyy-MM-dd)", schema = @Schema(format = "yyyy-MM-dd")),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "serviceType"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "subServiceType"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "model"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th "
							+ "index)"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is "
							+ "10") })
	@GetMapping("/get-filter-leads")
	public ResponseEntity<APIResponse<?>> getLeadsByFilter(Pagination pagination,
			@RequestParam(name = "startDueDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDueDate,
			@RequestParam(name = "endDueDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDueDate,
			@RequestParam(name = "serviceType", required = false) String serviceType,
			@RequestParam(name = "subServiceType", required = false) String subServiceType,
			@RequestParam(name = "model", required = false) String model) {
		return responseEntity(leadAllocationService.getAllfilteredLead(
				new LeadAllocationFilterRequest().builder().serviceType(serviceType).subServiceType(subServiceType)
						.endDueDate(endDueDate).startDueDate(startDueDate).model(model).build(),
				pagination));
	}

	@Operation(summary = "allocating leads to cre", description = "All active Leads", tags = {
			"Service Reminder - Follow-Up entries" }, parameters = {
					@Parameter(in = ParameterIn.PATH, required = true, name = TENANT) })
	@PostMapping("/allocate-leads")
	public ResponseEntity<APIResponse<?>> allocateLeadToCre(
			@Validated @RequestBody AllocateLeadToCre allocateLeadToCre) {
		return responseEntity(leadAllocationService.allocateLead(allocateLeadToCre));
	}

	@Operation(summary = "API for lead tranfer to another cre", description = "Transfer lead to another employee.", tags = {
			"Lead transfer" }, parameters = { @Parameter(in = ParameterIn.PATH, required = true, name = TENANT) })
	@PostMapping("/lead-transfer")
	public ResponseEntity<APIResponse<?>> leadTransferToAnotherEmp(
			@Validated @RequestBody LeadTransferRequest leadTransferRequest) {
		return responseEntity(leadAllocationService.leadTransferToAnotherCRE(leadTransferRequest));
	}

	@Operation(summary = "List of leads by filter", description = "All active Leads", tags = {
			"Service Reminder - Follow-Up entries" }, parameters = {
					@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "location"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "serviceCenterCode"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "empId"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "serviceType"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th "
							+ "index)"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is "
							+ "10") })
	@GetMapping("/filter-leads-transfer")
	public ResponseEntity<APIResponse<?>> getAllLeadsByFilter(Pagination pagination,
			@RequestParam(name = "location", required = false) String location,
			@RequestParam(name = "serviceCenterCode", required = false) String serviceCenterCode,
			@RequestParam(name = "empId", required = false) Integer empId,
			@RequestParam(name = "serviceType", required = false) String serviceType) {
		return responseEntity(leadAllocationService.getLeadTransferFilter(new LeadTransferFilterrequest().builder()
				.empId(empId).location(location).serviceCenterCode(serviceCenterCode).serviceType(serviceType).build(),
				pagination));
	}
	
//	@Scheduled(cron = "* */10 * * * *")
//	void checkForAnyNewLedsYetToBeAddedToLeadAllocation() {
//		addLedsToLeadAllocation();
//	}

	private void addLedsToLeadAllocation() {
		synchronized (lock1) {
			try {
				leadAllocationService.allocatedLeadByRoundRobin();
			} catch (Exception ex) {
				log.error("Exception while adding new employee entries into allocation table.");
			}
		}
	}
}