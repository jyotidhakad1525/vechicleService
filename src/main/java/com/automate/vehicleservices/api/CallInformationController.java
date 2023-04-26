package com.automate.vehicleservices.api;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.automate.vehicleservices.api.model.CallAuditFilterModel;
import com.automate.vehicleservices.api.model.MasterCallFilterModel;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.service.CallInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Call", description = "Customer call")
@RestController
@Slf4j
@RequestMapping(value = AbstractBaseController.BASE_URL + "/call")
public class CallInformationController extends AbstractBaseController {

	private final CallInfoService callInfoService;

	public CallInformationController(CallInfoService callInfoService) {
		this.callInfoService = callInfoService;
	}

	@Operation(summary = "call audit list", description = "All call audit customer list", tags = {
			"Service Reminder - Follow-Up entries" }, parameters = {
					@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th "
							+ "index)"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is "
							+ "10") })
	@GetMapping("/call-audit-list")
	public ResponseEntity<APIResponse<?>> getCallAuditList(Pagination pagination) {
		return responseEntity(callInfoService.getAllCallAudit(pagination));
	}

	@Operation(summary = "List of leads by filter", description = "All active Leads", tags = {
			"Service Reminder - Follow-Up entries" }, parameters = {
					@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "startDate", description = "startDate "
							+ "(yyyy-MM-dd)", schema = @Schema(format = "yyyy-MM-dd")),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "endDate", description = "endDate "
							+ "(yyyy-MM-dd)", schema = @Schema(format = "yyyy-MM-dd")),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "serviceCenterLocation"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "subCenterBranch"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "otherColumn"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th "
							+ "index)"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is "
							+ "10") })
	@GetMapping("/filter-call-audits")
	public ResponseEntity<APIResponse<?>> getLeadsByFilter(Pagination pagination,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(name = "serviceCenterLocation", required = false) String serviceCenterLocation,
			@RequestParam(name = "subCenterBranch", required = false) String subCenterBranch,
			@RequestParam(name = "otherColumn", required = false) String otherColumn) {
		return responseEntity(callInfoService.getfilteredCall(new CallAuditFilterModel().builder()
				.serviceCenterLocation(serviceCenterLocation).subCenterBranch(subCenterBranch).endDate(endDate)
				.startDate(startDate).otherColumn(otherColumn).build(), pagination));
	}

	@Operation(summary = "master call list", description = "All call audit customer list", tags = {
			"Service Reminder - Follow-Up entries" }, parameters = {
					@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th "
							+ "index)"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is "
							+ "10") })
	@GetMapping("/master-call-list")
	public ResponseEntity<APIResponse<?>> getAllMasterCallList(Pagination pagination) {
		return responseEntity(callInfoService.getAllMasterCalls(pagination));
	}

	@Operation(summary = "List of leads by filter", description = "All active Leads", tags = {
			"Service Reminder - Follow-Up entries" }, parameters = {
					@Parameter(in = ParameterIn.PATH, required = true, name = TENANT),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "startDueDate", description = "startDueDate "
							+ "(yyyy-MM-dd)", schema = @Schema(format = "yyyy-MM-dd")),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "endDueDate", description = "endDueDate "
							+ "(yyyy-MM-dd)", schema = @Schema(format = "yyyy-MM-dd")),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "location"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "serviceCenterCode"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "serviceType"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "model"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "page", description = "Default is 0. (0th "
							+ "index)"),
					@Parameter(in = ParameterIn.QUERY, required = false, name = "size", description = "Default page size is "
							+ "10") })
	@GetMapping("/filter-master-list")
	public ResponseEntity<APIResponse<?>> getMasterCallByFilter(Pagination pagination,
			@RequestParam(name = "startDueDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDueDate,
			@RequestParam(name = "endDueDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDueDate,
			@RequestParam(name = "location", required = false) String location,
			@RequestParam(name = "serviceCenterCode", required = false) String serviceCenterCode,
			@RequestParam(name = "serviceType", required = false) String serviceType,
			@RequestParam(name = "model", required = false) String model) {
		return responseEntity(callInfoService.getMasterCallFilter(new MasterCallFilterModel().builder()
				.startDueDate(startDueDate).endDueDate(endDueDate).location(location)
				.serviceCenterCode(serviceCenterCode).serviceType(serviceType).model(model).build(), pagination));
	}

}
