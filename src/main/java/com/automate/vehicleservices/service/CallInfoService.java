package com.automate.vehicleservices.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.vehicleservices.api.model.CallAuditFilterModel;
import com.automate.vehicleservices.api.model.CallAuditResponse;
import com.automate.vehicleservices.api.model.MasterCallAuditResponse;
import com.automate.vehicleservices.api.model.MasterCallFilterModel;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.LeadAllocationDetails;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.repository.CustomerRepository;
import com.automate.vehicleservices.repository.EmployeeRepository;
import com.automate.vehicleservices.repository.LeadAllocationRepo;
import com.automate.vehicleservices.repository.ServiceReminderFollowUpActivityRepository;
import com.automate.vehicleservices.repository.ServiceVehicleRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;

@Service
public class CallInfoService extends AbstractService {

	private final ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository;
	private final CustomerRepository customerRepository;
	private final ServiceVehicleRepository serviceVehicleRepository;
	private final EmployeeRepository employeeRepository;
	private final LeadAllocationRepo leadAllocationRepo;

	public CallInfoService(ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository,
			CustomerRepository customerRepository, ServiceVehicleRepository serviceVehicleRepository,
			EmployeeRepository employeeRepository, LeadAllocationRepo leadAllocationRepo) {
		this.serviceReminderFollowUpActivityRepository = serviceReminderFollowUpActivityRepository;
		this.customerRepository = customerRepository;
		this.serviceVehicleRepository = serviceVehicleRepository;
		this.employeeRepository = employeeRepository;
		this.leadAllocationRepo = leadAllocationRepo;
	}

	public PaginatedSearchResponse<CallAuditResponse> getAllCallAudit(Pagination pagination) {
		Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(),
				Sort.by("FOLLOW_UP_DATE").descending());
		final var pageResult = serviceReminderFollowUpActivityRepository.findLeadsForCallAudit(pageable);
//		System.out.println(pageResult.getContent().stream().toList());
		final var list = pageResult.getContent().stream().map(data -> new CallAuditResponse(searchResponse(data)))
				.collect(Collectors.toList());
		return new PaginatedSearchResponse<>(pageResult, list);
	}

	public CallAuditResponse searchResponse(ServiceReminderFollowUpActivity l) {
		CallAuditResponse data = new CallAuditResponse();
		Integer customerId = l.getServiceReminderFollowUp().getCustomer().getId();
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isPresent()) {
			data.setId(l.getId());
			data.setCustomerName(customer.get().getFirstName());
			data.setMobileNumber(customer.get().getContactNumber());
			Integer vechileId = l.getServiceReminderFollowUp().getServiceVehicle().getId();
			Optional<ServiceVehicle> serviceVehicle = serviceVehicleRepository.findById(vechileId);
			if (serviceVehicle.isPresent()) {
				data.setRegNumber(serviceVehicle.get().getRegNumber());
			}
			data.setServiceType(l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
					.getMdServiceType().getMdServiceCategory().getCategoryName());
			data.setSubServiceType(l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
					.getMdServiceType().getServiceName());
			List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
					.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
			if (findByServiceVehicleId.size() > 0) {
				Integer employeeId = findByServiceVehicleId.get(0).getCreDetail().getId();
				Optional<Employee> employee = employeeRepository.findById(employeeId);
				if (employee.isPresent()) {
					data.setCreName(employee.get().getName());
					data.setLocation(findByServiceVehicleId.get(0).getCreLocation());
				}
			}
			data.setCallDateTime(l.getFollowUpDate());
			data.setFeedback(l.getFollowUpActivityResult());

		}
		return data;
	}

	@Transactional
	public PaginatedSearchResponse<CallAuditResponse> getfilteredCall(CallAuditFilterModel callAuditFilterModel,
			Pagination pagination) {
		Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.unsorted());
		List<ServiceReminderFollowUpActivity> listOfLeads = callAuditFilter(callAuditFilterModel);
		final var pageResult = new PageImpl<ServiceReminderFollowUpActivity>(listOfLeads, pageable, listOfLeads.size());
//		System.out.println(pageResult.getContent().stream().toList());
		final var list = pageResult.getContent().stream().map(data -> new CallAuditResponse(searchResponse(data)))
				.collect(Collectors.toList());
		return new PaginatedSearchResponse<>(pageResult, list);
	}

	public List<ServiceReminderFollowUpActivity> callAuditFilter(CallAuditFilterModel callAuditFilterModel) {
		List<ServiceReminderFollowUpActivity> serviceFollowUp = new ArrayList<>();
		List<ServiceReminderFollowUpActivity> serviceReminderFollowUpActivity = (List<ServiceReminderFollowUpActivity>) serviceReminderFollowUpActivityRepository
				.findLeadsForCallAudit();
		if (callAuditFilterModel.getStartDate() != null && callAuditFilterModel.getEndDate() != null) {
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				LocalDate followUpDate = l.getFollowUpDate().toLocalDate();
				if (followUpDate.isAfter(callAuditFilterModel.getStartDate())
						&& followUpDate.isBefore(callAuditFilterModel.getEndDate())) {
					serviceFollowUp.add(l);
				}
			}
		}
		if (callAuditFilterModel.getStartDate() != null && callAuditFilterModel.getEndDate() != null
				&& callAuditFilterModel.getServiceCenterLocation() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				LocalDate followUpDate = l.getFollowUpDate().toLocalDate();
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					String serviceCenterLocation = findByServiceVehicleId.get(0).getCreLocation();
					if (followUpDate.isAfter(callAuditFilterModel.getStartDate())
							&& followUpDate.isBefore(callAuditFilterModel.getEndDate())
							&& serviceCenterLocation.equals(callAuditFilterModel.getServiceCenterLocation())) {
						serviceFollowUp.add(l);
					}
				}
			}
		}
		if (callAuditFilterModel.getStartDate() != null && callAuditFilterModel.getEndDate() != null
				&& callAuditFilterModel.getServiceCenterLocation() != null
				&& callAuditFilterModel.getSubCenterBranch() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				LocalDate followUpDate = l.getFollowUpDate().toLocalDate();
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					String serviceCenterLocation = findByServiceVehicleId.get(0).getCreLocation();
					String subCenterBranch = l.getServiceReminderFollowUp().getServiceReminderDetails()
							.getServiceReminder().getMdServiceType().getMdTenant().getTenantName();
					if (followUpDate.isAfter(callAuditFilterModel.getStartDate())
							&& followUpDate.isBefore(callAuditFilterModel.getEndDate())
							&& serviceCenterLocation.equals(callAuditFilterModel.getServiceCenterLocation())
							&& subCenterBranch.equals(callAuditFilterModel.getSubCenterBranch())) {
						serviceFollowUp.add(l);
					}
				}
			}
		}
		return serviceFollowUp;
	}

	public PaginatedSearchResponse<MasterCallAuditResponse> getAllMasterCalls(Pagination pagination) {
		Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.unsorted());
		final var pageResult = serviceReminderFollowUpActivityRepository.findMasterCall(pageable);
		final var list = pageResult.getContent().stream()
				.map(data -> new MasterCallAuditResponse(searchMasterResponse(data))).collect(Collectors.toList());
		return new PaginatedSearchResponse<>(pageResult, list);
	}

	public MasterCallAuditResponse searchMasterResponse(ServiceReminderFollowUpActivity l) {
		MasterCallAuditResponse data = new MasterCallAuditResponse();
		Integer customerId = l.getServiceReminderFollowUp().getCustomer().getId();
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isPresent()) {
			data.setId(l.getId());
			List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
					.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
			if (findByServiceVehicleId.size() > 0) {
				data.setLocation(findByServiceVehicleId.get(0).getCreLocation());
				data.setServiceCenterCode(findByServiceVehicleId.get(0).getServiceCenterCode());
				data.setCreName(findByServiceVehicleId.get(0).getCreDetail().getName());
			}
			data.setServiceType(l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
					.getMdServiceType().getMdServiceCategory().getCategoryName());
			data.setFirstName(customer.get().getFirstName());
			data.setLastName(customer.get().getLastName());
			data.setMobileNumber(customer.get().getContactNumber());
			if (customer.get().getCustomerAddresses().size() > 0) {
				data.setPinCode(customer.get().getCustomerAddresses().get(0).getPin());
			}
			data.setModel(l.getServiceReminderFollowUp().getServiceVehicle().getModel());
			data.setVinNumber(l.getServiceReminderFollowUp().getServiceVehicle().getVin());
			data.setRegNumber(l.getServiceReminderFollowUp().getServiceVehicle().getRegNumber());
			if (l.getServiceReminderFollowUp().getServiceVehicle().getVehicleKmTrackers().size() > 0) {
				data.setCurrentMileage(String.valueOf(l.getServiceReminderFollowUp().getServiceVehicle()
						.getVehicleKmTrackers().get(0).getKmReading()));
			}
			data.setVehicleSaleDate(null);
			data.setLastServiceDate(l.getEndDate());
		}
		return data;
	}

	@Transactional
	public PaginatedSearchResponse<MasterCallAuditResponse> getMasterCallFilter(
			MasterCallFilterModel masterCallFilterModel, Pagination pagination) {
		Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.unsorted());
		List<ServiceReminderFollowUpActivity> listOfLeads = MasrterCallFilter(masterCallFilterModel);
		final var pageResult = new PageImpl<ServiceReminderFollowUpActivity>(listOfLeads, pageable, listOfLeads.size());
		final var list = pageResult.getContent().stream()
				.map(data -> new MasterCallAuditResponse(searchMasterResponse(data))).collect(Collectors.toList());
		return new PaginatedSearchResponse<>(pageResult, list);
	}

	public List<ServiceReminderFollowUpActivity> MasrterCallFilter(MasterCallFilterModel masterCallFilterModel) {
		List<ServiceReminderFollowUpActivity> serviceFollowUp = new ArrayList<>();
		List<ServiceReminderFollowUpActivity> serviceReminderFollowUpActivity = (List<ServiceReminderFollowUpActivity>) serviceReminderFollowUpActivityRepository
				.findMasterCallList();
		if (masterCallFilterModel.getStartDueDate() != null && masterCallFilterModel.getEndDueDate() != null) {
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				LocalDate followUpDate = l.getEndDate();
				if (followUpDate.isAfter(masterCallFilterModel.getStartDueDate())
						&& followUpDate.isBefore(masterCallFilterModel.getEndDueDate())) {
					serviceFollowUp.add(l);
				}
			}
		}
		if (masterCallFilterModel.getStartDueDate() != null && masterCallFilterModel.getEndDueDate() != null
				&& masterCallFilterModel.getLocation() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				LocalDate followUpDate = l.getEndDate();
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					String serviceCenterLocation = findByServiceVehicleId.get(0).getCreLocation();
					if (followUpDate.isAfter(masterCallFilterModel.getStartDueDate())
							&& followUpDate.isBefore(masterCallFilterModel.getEndDueDate())
							&& serviceCenterLocation.equals(masterCallFilterModel.getLocation())) {
						serviceFollowUp.add(l);
					}
				}
			}
		}
		if (masterCallFilterModel.getStartDueDate() != null && masterCallFilterModel.getEndDueDate() != null
				&& masterCallFilterModel.getLocation() != null
				&& masterCallFilterModel.getServiceCenterCode() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				LocalDate followUpDate = l.getEndDate();
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					String serviceCenterLocation = findByServiceVehicleId.get(0).getCreLocation();
					String serviceCenterCode = findByServiceVehicleId.get(0).getServiceCenterCode();
					if (followUpDate.isAfter(masterCallFilterModel.getStartDueDate())
							&& followUpDate.isBefore(masterCallFilterModel.getEndDueDate())
							&& serviceCenterLocation.equals(masterCallFilterModel.getLocation())
							&& serviceCenterCode.equals(masterCallFilterModel.getServiceCenterCode())) {
						serviceFollowUp.add(l);
					}
				}
			}
		}
		if (masterCallFilterModel.getStartDueDate() != null && masterCallFilterModel.getEndDueDate() != null
				&& masterCallFilterModel.getLocation() != null && masterCallFilterModel.getServiceCenterCode() != null
				&& masterCallFilterModel.getServiceType() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				LocalDate followUpDate = l.getEndDate();
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					String serviceCenterLocation = findByServiceVehicleId.get(0).getCreLocation();
					String serviceCenterCode = findByServiceVehicleId.get(0).getServiceCenterCode();
					String serviceType = l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
							.getMdServiceType().getMdServiceCategory().getCategoryName();
					if (followUpDate.isAfter(masterCallFilterModel.getStartDueDate())
							&& followUpDate.isBefore(masterCallFilterModel.getEndDueDate())
							&& serviceCenterLocation.equals(masterCallFilterModel.getLocation())
							&& serviceCenterCode.equals(masterCallFilterModel.getServiceCenterCode())
							&& serviceType.equals(masterCallFilterModel.getServiceType())) {
						serviceFollowUp.add(l);
					}
				}
			}
		}
		if (masterCallFilterModel.getStartDueDate() != null && masterCallFilterModel.getEndDueDate() != null
				&& masterCallFilterModel.getLocation() != null && masterCallFilterModel.getServiceCenterCode() != null
				&& masterCallFilterModel.getServiceType() != null && masterCallFilterModel.getModel() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				List<ServiceReminderFollowUpActivity> service = new ArrayList<>();
				LocalDate followUpDate = l.getEndDate();

				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					String serviceCenterLocation = findByServiceVehicleId.get(0).getCreLocation();
					String serviceCenterCode = findByServiceVehicleId.get(0).getServiceCenterCode();
					String serviceType = l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
							.getMdServiceType().getMdServiceCategory().getCategoryName();
					String model = l.getServiceReminderFollowUp().getServiceVehicle().getModel();
					if (followUpDate.isAfter(masterCallFilterModel.getStartDueDate())
							&& followUpDate.isBefore(masterCallFilterModel.getEndDueDate())
							&& serviceCenterLocation.equals(masterCallFilterModel.getLocation())
							&& serviceCenterCode.equals(masterCallFilterModel.getServiceCenterCode())
							&& serviceType.equals(masterCallFilterModel.getServiceType())
							&& model.equals(masterCallFilterModel.getModel())) {
						service.add(l);
						serviceFollowUp.addAll(service);
					}
				}
			}
		}
		return serviceFollowUp;
	}
}
