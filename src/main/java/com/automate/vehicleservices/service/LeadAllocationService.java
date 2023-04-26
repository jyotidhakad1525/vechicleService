package com.automate.vehicleservices.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.automate.vehicleservices.api.model.AllocateLeadToCre;
import com.automate.vehicleservices.api.model.LeadAllocationFilterRequest;
import com.automate.vehicleservices.api.model.LeadTransferFilterrequest;
import com.automate.vehicleservices.api.model.LeadTransferRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.Employee;
import com.automate.vehicleservices.entity.LeadAllocationDetails;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.OrgDataAllocationStrategyType;
import com.automate.vehicleservices.entity.RoundRobinDataAllocationStrategy;
import com.automate.vehicleservices.entity.ServiceReminderFollowUp;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.ServiceTypeBasedAllocationRatio;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.automate.vehicleservices.repository.CustomerRepository;
import com.automate.vehicleservices.repository.EmployeeRepository;
import com.automate.vehicleservices.repository.LeadAllocationRepo;
import com.automate.vehicleservices.repository.MdOrganizationRepository;
import com.automate.vehicleservices.repository.MdServiceTypeRepository;
import com.automate.vehicleservices.repository.OrgDataAllocationStrategyTypeRepository;
import com.automate.vehicleservices.repository.RoundRobinDataAllocationStrategyRepository;
import com.automate.vehicleservices.repository.ServiceReminderFollowUpActivityRepository;
import com.automate.vehicleservices.repository.ServiceReminderFollowUpRepository;
import com.automate.vehicleservices.repository.ServiceTypeBasedAllocationRatioRepository;
import com.automate.vehicleservices.repository.ServiceVehicleRepository;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.SearchResponseServiceDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LeadAllocationService extends AbstractService {

	private final ServiceVehicleRepository serviceVehicleRepository;
	private final MdServiceTypeRepository mdServiceTypeRepository;
	private final RoundRobinDataAllocationStrategyRepository roundRobinDataRepository;
	private final ServiceTypeBasedAllocationRatioRepository serviceTypeBasedAllocationRatioRepository;
	private final CustomerRepository customerRepository;
	private final ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository;
	private final EmployeeRepository employeeRepository;
	private final ServiceReminderFollowUpRepository serviceReminderFollowUpRepository;
	private final OrgDataAllocationStrategyTypeRepository orgTypeRepository;
	private final MdOrganizationRepository mdOrganizationRepository;
	private final LeadAllocationRepo leadAllocationRepo;

	public LeadAllocationService(ServiceVehicleRepository serviceVehicleRepository,
			MdServiceTypeRepository mdServiceTypeRepository, CustomerRepository customerRepository,
			ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository,
			EmployeeRepository employeeRepository, ServiceReminderFollowUpRepository serviceReminderFollowUpRepository,
			OrgDataAllocationStrategyTypeRepository orgTypeRepository,
			RoundRobinDataAllocationStrategyRepository roundRobinDataRepository,
			ServiceTypeBasedAllocationRatioRepository serviceTypeBasedAllocationRatioRepository,
			MdOrganizationRepository mdOrganizationRepository, LeadAllocationRepo leadAllocationRepo) {
		this.serviceVehicleRepository = serviceVehicleRepository;
		this.mdServiceTypeRepository = mdServiceTypeRepository;
		this.customerRepository = customerRepository;
		this.serviceReminderFollowUpActivityRepository = serviceReminderFollowUpActivityRepository;
		this.employeeRepository = employeeRepository;
		this.serviceReminderFollowUpRepository = serviceReminderFollowUpRepository;
		this.orgTypeRepository = orgTypeRepository;
		this.roundRobinDataRepository = roundRobinDataRepository;
		this.serviceTypeBasedAllocationRatioRepository = serviceTypeBasedAllocationRatioRepository;
		this.mdOrganizationRepository = mdOrganizationRepository;
		this.leadAllocationRepo = leadAllocationRepo;
	}

	@Transactional
	public PaginatedSearchResponse<SearchResponseServiceDto> getAllLead(Pagination pagination) {
		Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.unsorted());
		final var pageResult = serviceReminderFollowUpActivityRepository.findUnAllocatedLeads(pageable);
		final var list = pageResult.getContent().stream()
				.map(data -> new SearchResponseServiceDto(serviceLeadResponse(data))).collect(Collectors.toList());
		return new PaginatedSearchResponse<>(pageResult, list);
	}

	public SearchResponseServiceDto serviceLeadResponse(ServiceReminderFollowUpActivity l) {
		SearchResponseServiceDto data = new SearchResponseServiceDto();
		Integer customerId = l.getServiceReminderFollowUp().getCustomer().getId();
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isPresent()) {
			data.setId(l.getId());
			data.setFirstName(customer.get().getFirstName());
			data.setLastName(customer.get().getLastName());
			data.setMobileNo(customer.get().getContactNumber());
			Integer vechileId = l.getServiceReminderFollowUp().getServiceVehicle().getId();
			Optional<ServiceVehicle> serviceVehicle = serviceVehicleRepository.findById(vechileId);
			if (serviceVehicle.isPresent()) {
				data.setModel(serviceVehicle.get().getModel());
				data.setVinNo(serviceVehicle.get().getVin());
				data.setRegNo(serviceVehicle.get().getRegNumber());
				data.setSellingDate(serviceVehicle.get().getPurchaseDate());
				if(serviceVehicle.get().getServiceType()!=null) {
					data.setServiceType(serviceVehicle.get().getServiceType()
							.getMdServiceCategory().getCategoryName());
				}
			}
			data.setDueDate(l.getServiceReminderFollowUp().getServiceVehicle().getDueDate());
			
			data.setSubServiceType(l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
					.getMdServiceType().getServiceName());
		}
		return data;
	}

	@Transactional
	public PaginatedSearchResponse<SearchResponseServiceDto> getAllfilteredLead(
			LeadAllocationFilterRequest LeadAllocationFilterRequest, Pagination pagination) {
		Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.unsorted());
		List<ServiceReminderFollowUpActivity> listOfLeads = getLeadByFilter(LeadAllocationFilterRequest);
		final var pageResult = new PageImpl<ServiceReminderFollowUpActivity>(listOfLeads, pageable, listOfLeads.size());
		final var list = pageResult.getContent().stream()
				.map(data -> new SearchResponseServiceDto(serviceLeadResponse(data))).collect(Collectors.toList());
		return new PaginatedSearchResponse<>(pageResult, list);
	}

	public List<ServiceReminderFollowUpActivity> getLeadByFilter(
			LeadAllocationFilterRequest leadAllocationFilterRequest) {
		List<ServiceReminderFollowUpActivity> serviceFollowUp = new ArrayList<>();
		List<ServiceReminderFollowUpActivity> serviceReminderFollowUpActivity = (List<ServiceReminderFollowUpActivity>) serviceReminderFollowUpActivityRepository
				.findUnAllocatedLeads();
		if (leadAllocationFilterRequest.getStartDueDate() != null
				&& leadAllocationFilterRequest.getEndDueDate() != null) {
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				List<ServiceReminderFollowUpActivity> service = new ArrayList<>();
				// LocalDate dueDate = l.getEndDate();
				LocalDate dueDate = l.getServiceReminderFollowUp().getServiceVehicle().getDueDate();
				if (dueDate.isAfter(leadAllocationFilterRequest.getStartDueDate())
						&& dueDate.isBefore(leadAllocationFilterRequest.getEndDueDate())) {
					service.add(l);
					serviceFollowUp.addAll(service);
				}
			}
		}
		if (leadAllocationFilterRequest.getStartDueDate() != null && leadAllocationFilterRequest.getEndDueDate() != null
				&& leadAllocationFilterRequest.getServiceType() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				List<ServiceReminderFollowUpActivity> service = new ArrayList<>();
				LocalDate dueDate = l.getServiceReminderFollowUp().getServiceVehicle().getDueDate();
				String serviceType = l.getServiceReminderFollowUp().getServiceVehicle().getServiceType()
						.getMdServiceCategory().getCategoryName();
				if (dueDate.isAfter(leadAllocationFilterRequest.getStartDueDate())
						&& dueDate.isBefore(leadAllocationFilterRequest.getEndDueDate())
						&& serviceType.equals(leadAllocationFilterRequest.getServiceType())) {
					service.add(l);
					serviceFollowUp.addAll(service);
				}
			}
		}
		if (leadAllocationFilterRequest.getStartDueDate() != null && leadAllocationFilterRequest.getEndDueDate() != null
				&& leadAllocationFilterRequest.getServiceType() != null
				&& leadAllocationFilterRequest.getSubServiceType() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				List<ServiceReminderFollowUpActivity> service = new ArrayList<>();
				LocalDate dueDate = l.getServiceReminderFollowUp().getServiceVehicle().getDueDate();
				String serviceType = l.getServiceReminderFollowUp().getServiceVehicle().getServiceType()
						.getMdServiceCategory().getCategoryName();
				String subServiceTyp = l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
						.getMdServiceType().getServiceName();
				if (dueDate.isAfter(leadAllocationFilterRequest.getStartDueDate())
						&& dueDate.isBefore(leadAllocationFilterRequest.getEndDueDate())
						&& serviceType.equals(leadAllocationFilterRequest.getServiceType())
						&& subServiceTyp.equals(leadAllocationFilterRequest.getSubServiceType())) {
					service.add(l);
					serviceFollowUp.addAll(service);
				}
			}
		}
		if (leadAllocationFilterRequest.getStartDueDate() != null && leadAllocationFilterRequest.getEndDueDate() != null
				&& leadAllocationFilterRequest.getServiceType() != null
				&& leadAllocationFilterRequest.getSubServiceType() != null
				&& leadAllocationFilterRequest.getModel() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : serviceReminderFollowUpActivity) {
				List<ServiceReminderFollowUpActivity> service = new ArrayList<>();
				LocalDate dueDate = l.getServiceReminderFollowUp().getServiceVehicle().getDueDate();
				String serviceType = l.getServiceReminderFollowUp().getServiceVehicle().getServiceType()
						.getMdServiceCategory().getCategoryName();
				String subServiceTyp = l.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder()
						.getMdServiceType().getServiceName();
				String model = l.getServiceReminderFollowUp().getServiceVehicle().getModel();
				if (dueDate.isAfter(leadAllocationFilterRequest.getStartDueDate())
						&& dueDate.isBefore(leadAllocationFilterRequest.getEndDueDate())
						&& serviceType.equals(leadAllocationFilterRequest.getServiceType())
						&& subServiceTyp.equals(leadAllocationFilterRequest.getSubServiceType())
						&& model.equals(leadAllocationFilterRequest.getModel())) {
					service.add(l);
					serviceFollowUp.addAll(service);
				}
			}
		}
		return serviceFollowUp;
	}

	public List<ServiceReminderFollowUpActivity> leadTransferToAnotherCRE(LeadTransferRequest leadTransferRequest) {
		List<ServiceReminderFollowUpActivity> response = new ArrayList<>();
		List<Integer> leadId = leadTransferRequest.getFollowUpId();
		if (leadTransferRequest.getLocation() != null || leadTransferRequest.getServiceCenterCode() != null
				|| leadTransferRequest.getEmployeeId() != 0) {
			for (Integer lead : leadId) {
				ServiceReminderFollowUpActivity serviceLead = serviceReminderFollowUpActivityRepository.findById(lead)
						.get();
				Employee employee = employeeRepository.findById(leadTransferRequest.getEmployeeId()).get();
				serviceLead.setCre(employee);
				serviceReminderFollowUpActivityRepository.save(serviceLead);
//				serviceLead.setCreLocation(leadTransferRequest.getLocation());
//				serviceLead.setServiceCenterCode(leadTransferRequest.getServiceCenterCode());
				serviceReminderFollowUpActivityRepository.save(serviceLead);
				Integer id = serviceLead.getServiceReminderFollowUp().getId();
				ServiceReminderFollowUp serviceReminderFollowUp = serviceReminderFollowUpRepository.findById(id).get();
				serviceReminderFollowUp.setCre(employee);
				serviceReminderFollowUpRepository.save(serviceReminderFollowUp);
				response.add(serviceLead);
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(serviceLead.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					for (LeadAllocationDetails lad : findByServiceVehicleId) {
						lad.setStatus(ActiveInActiveStatus.INACTIVE);
						leadAllocationRepo.save(lad);
					}
				}
				LeadAllocationDetails lad = new LeadAllocationDetails();
				lad.setCreDetail(employee);
				lad.setCreLocation(leadTransferRequest.getLocation());
				lad.setServiceCenterCode(leadTransferRequest.getServiceCenterCode());
				lad.setVehicle(serviceLead.getServiceReminderFollowUp().getServiceVehicle());
				lad.setStatus(ActiveInActiveStatus.ACTIVE);
				lad.setCreatedDatetime(new Date(System.currentTimeMillis()));
				lad.setUpdatedDatetime(new Date(System.currentTimeMillis()));
				leadAllocationRepo.save(lad);
			}
		}
		return response;
	}

	public List<ServiceReminderFollowUpActivity> allocateLead(AllocateLeadToCre allocateLeadToCre) {
		List<ServiceReminderFollowUpActivity> response = new ArrayList<>();
		List<ServiceReminderFollowUpActivity> lead = (List<ServiceReminderFollowUpActivity>) serviceReminderFollowUpActivityRepository
				.findAllById(allocateLeadToCre.getFollowUpIds());
		for (ServiceReminderFollowUpActivity serviceLead : lead) {
			Employee employee = employeeRepository.findById(allocateLeadToCre.getCreId()).get();
			serviceLead.setCre(employee);
//			serviceLead.setCreLocation(allocateLeadToCre.getLocation());
//			serviceLead.setServiceCenterCode(allocateLeadToCre.getServiceCenterCode());
			Integer id = serviceLead.getServiceReminderFollowUp().getId();
			ServiceReminderFollowUp serviceReminderFollowUp = serviceReminderFollowUpRepository.findById(id).get();
			serviceReminderFollowUp.setCre(employee);
			serviceReminderFollowUpRepository.save(serviceReminderFollowUp);
			serviceReminderFollowUpActivityRepository.save(serviceLead);
			LeadAllocationDetails lad = new LeadAllocationDetails();
			lad.setCreDetail(employee);
			lad.setCreLocation(allocateLeadToCre.getLocation());
			lad.setServiceCenterCode(allocateLeadToCre.getServiceCenterCode());
			lad.setVehicle(serviceLead.getServiceReminderFollowUp().getServiceVehicle());
			lad.setStatus(ActiveInActiveStatus.ACTIVE);
			lad.setCreatedDatetime(new Date(System.currentTimeMillis()));
			lad.setUpdatedDatetime(new Date(System.currentTimeMillis()));
			leadAllocationRepo.save(lad);
			response.add(serviceLead);
		}
		return response;
	}

	@Transactional
	public PaginatedSearchResponse<SearchResponseServiceDto> getLeadTransferFilter(
			LeadTransferFilterrequest leadTransferFilterrequest, Pagination pagination) {
		Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.unsorted());
		List<ServiceReminderFollowUpActivity> listOfLeads = leadTransferFilter(leadTransferFilterrequest);
		final var pageResult = new PageImpl<ServiceReminderFollowUpActivity>(listOfLeads, pageable, listOfLeads.size());
		final var list = pageResult.getContent().stream()
				.map(data -> new SearchResponseServiceDto(serviceLeadResponse(data))).collect(Collectors.toList());
		return new PaginatedSearchResponse<>(pageResult, list);
	}

	public List<ServiceReminderFollowUpActivity> leadTransferFilter(LeadTransferFilterrequest leadTransferFilterrequest) {
		List<ServiceReminderFollowUpActivity> serviceFollowUp = new ArrayList<>();
		List<ServiceReminderFollowUpActivity> allLeads = (List<ServiceReminderFollowUpActivity>) serviceReminderFollowUpActivityRepository
				.findAllocatedLeads();
		if (leadTransferFilterrequest.getLocation() != null
				&& leadTransferFilterrequest.getServiceCenterCode() != null) {
			for (ServiceReminderFollowUpActivity l : allLeads) {
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if(findByServiceVehicleId.size()>0) {
					String location = findByServiceVehicleId.get(0).getCreLocation();
					String serviceCenterCode = findByServiceVehicleId.get(0).getServiceCenterCode();
					if (location!= null &&serviceCenterCode!= null) {
						if (location.equalsIgnoreCase(leadTransferFilterrequest.getLocation())
								&& serviceCenterCode.equalsIgnoreCase(leadTransferFilterrequest.getServiceCenterCode())) {
							serviceFollowUp.add(l);
						}
					}
				}
			}
		}
		if (leadTransferFilterrequest.getLocation() != null && leadTransferFilterrequest.getServiceCenterCode() != null
				&& leadTransferFilterrequest.getEmpId() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : allLeads) {
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if(findByServiceVehicleId.size()>0) {
					String location = findByServiceVehicleId.get(0).getCreLocation();
					String serviceCenterCode = findByServiceVehicleId.get(0).getServiceCenterCode();
					Integer creId=findByServiceVehicleId.get(0).getCreDetail().getId();
					if (location!= null &&serviceCenterCode!= null&&creId!=null) {
							if (location.equalsIgnoreCase(leadTransferFilterrequest.getLocation())
									&& serviceCenterCode.equalsIgnoreCase(leadTransferFilterrequest.getServiceCenterCode())
									&& creId == leadTransferFilterrequest.getEmpId()) {
								serviceFollowUp.add(l);
							}
					}
				}
			}
		}
		if (leadTransferFilterrequest.getLocation() != null && leadTransferFilterrequest.getServiceCenterCode() != null
				&& leadTransferFilterrequest.getEmpId() != null
				&& leadTransferFilterrequest.getServiceType() != null) {
			serviceFollowUp.clear();
			for (ServiceReminderFollowUpActivity l : allLeads) {
				List<LeadAllocationDetails> findByServiceVehicleId = leadAllocationRepo
						.findByServiceVehicleId(l.getServiceReminderFollowUp().getServiceVehicle().getId());
				if (findByServiceVehicleId.size() > 0) {
					String location = findByServiceVehicleId.get(0).getCreLocation();
					String serviceCenterCode = findByServiceVehicleId.get(0).getServiceCenterCode();
					Integer creId = findByServiceVehicleId.get(0).getCreDetail().getId();
					String serviceType = findByServiceVehicleId.get(0).getVehicle().getServiceType()
							.getMdServiceCategory().getCategoryName();
					if (location != null && serviceCenterCode != null && creId != null && serviceType != null) {
						if (location.equalsIgnoreCase(leadTransferFilterrequest.getLocation())
								&& serviceCenterCode.equalsIgnoreCase(leadTransferFilterrequest.getServiceCenterCode())
								&& creId == leadTransferFilterrequest.getEmpId()
								&& serviceType.equalsIgnoreCase(leadTransferFilterrequest.getServiceType())) {
							serviceFollowUp.add(l);
						}
					}
				}
			}
		}
		return serviceFollowUp;
	}

	public void allocatedLeadByRoundRobin() {

		List<MdOrganization> allOrganization = (List<MdOrganization>) mdOrganizationRepository.findAll();
		for (MdOrganization O : allOrganization) {
			Optional<OrgDataAllocationStrategyType> findAllByOrganizationIdAndStatusAndAllocationType = orgTypeRepository
					.findAllByOrganizationIdAndStatus(O.getId(), ActiveInActiveStatus.ACTIVE);
			if (findAllByOrganizationIdAndStatusAndAllocationType.isPresent()) {
				List<MdServiceType> serviceTypeByOrgIdentifier = mdServiceTypeRepository
						.getServiceTypeByOrgIdentifier(O.getId());
				for (MdServiceType mst : serviceTypeByOrgIdentifier) {
					List<RoundRobinDataAllocationStrategy> creList = roundRobinDataRepository
							.getCREDetailsBasedOnOrgIdAndStatusAndServiceType(O.getId(), mst.getId());
					List<ServiceReminderFollowUpActivity> leadList = serviceReminderFollowUpActivityRepository
							.findUnAllocatedLeadsByOrgIdServiceType(O.getId(), mst.getId());
					int totalLeadCount = leadList.size();
					int creCount = creList.size();
					if (totalLeadCount > 0 && creCount > 0) {
						int[] leadAssignment = new int[creCount];
						if (!creList.get(0).getIsNumber()) {
							int[] ratios = new int[creCount];
							int ratioSum = 0;
							for (int i = 0; i < creCount; i++) {
								List<ServiceTypeBasedAllocationRatio> allAllocationRatios = serviceTypeBasedAllocationRatioRepository
										.getServiceTypeByRoundRobinId(creList.get(i).getId());
								for (ServiceTypeBasedAllocationRatio currentAllocationRatio : allAllocationRatios) {
									if (currentAllocationRatio.getServiceTypes().getId() == mst.getId()) {
										ratios[i] = currentAllocationRatio.getRatio();
										ratioSum += ratios[i];
									}
								}
							}
							if (ratioSum > 0) {
								for (int i = 0; i < ratios.length; i++) {
									leadAssignment[i] =new BigDecimal(ratios[i]).divide(new BigDecimal(ratioSum))
											.multiply(new BigDecimal(totalLeadCount)).intValue(); // changes here and lead alocation
								}
							}
						} else {
							for (int i = 0; i < creCount; i++) {
								List<ServiceTypeBasedAllocationRatio> allAllocationRatios = serviceTypeBasedAllocationRatioRepository
										.getServiceTypeByRoundRobinId(creList.get(i).getId());
								for (ServiceTypeBasedAllocationRatio currentAllocationRatio : allAllocationRatios) {
									if (currentAllocationRatio.getServiceTypes().getId() == mst.getId()) {
										leadAssignment[i] = currentAllocationRatio.getRatio();
										// here ratio is actually a number so we can directly assign in the
										// leadAssignment array
									}
								}
							}
						}
						int leadAssignmentStartIndex = 0;
						int leadAssignmentEndIndex = 0;
						Map<Integer, List<ServiceReminderFollowUpActivity>> map = new HashMap<>();
						for (int i = 0; i < leadAssignment.length; i++) {
							leadAssignmentStartIndex = leadAssignmentEndIndex;
							leadAssignmentEndIndex = leadAssignment[i] + leadAssignmentStartIndex;
							Integer creId = creList.get(i).getEmployee().getId();
							leadAssignmentEndIndex = leadAssignmentEndIndex > (totalLeadCount) ? totalLeadCount
									: leadAssignmentEndIndex;
							map.put(creId, leadList.subList(leadAssignmentStartIndex, leadAssignmentEndIndex));
						}
						map.entrySet().stream().forEach(c -> {
							List<ServiceReminderFollowUpActivity> list = c.getValue();
							for (ServiceReminderFollowUpActivity serviceLead : list) {
								Employee employee = employeeRepository.findById(c.getKey()).get();
								serviceLead.setCre(employee);
//								serviceLead.setCreLocation(employee.getTenant().getCity());
//								serviceLead.setServiceCenterCode(String.valueOf(employee.getTenant().getMdOrganization().getId()));
								Integer id = serviceLead.getServiceReminderFollowUp().getId();
								ServiceReminderFollowUp serviceReminderFollowUp = serviceReminderFollowUpRepository
										.findById(id).get();
								serviceReminderFollowUp.setCre(employee);
								serviceReminderFollowUpRepository.save(serviceReminderFollowUp);
								serviceReminderFollowUpActivityRepository.save(serviceLead);
								LeadAllocationDetails lad = new LeadAllocationDetails();
								lad.setCreDetail(employee);
								lad.setCreLocation(employee.getTenant().getCity());
								lad.setServiceCenterCode(String.valueOf(employee.getTenant().getMdOrganization().getId()));
								lad.setVehicle(serviceLead.getServiceReminderFollowUp().getServiceVehicle());
								lad.setStatus(ActiveInActiveStatus.ACTIVE);
								lad.setCreatedDatetime(new Date(System.currentTimeMillis()));
								lad.setUpdatedDatetime(new Date(System.currentTimeMillis()));
								leadAllocationRepo.save(lad);
							}
						});
					}
				}

			}
		}
	}
}