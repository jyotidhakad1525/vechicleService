package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.FollowUpMasterDataType;
import com.automate.vehicleservices.api.model.followup.*;
import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.builder.ServiceReminderFollowUpActivityBuilder;
import com.automate.vehicleservices.entity.builder.ServiceReminderFollowUpBuilder;
import com.automate.vehicleservices.entity.builder.ServiceReminderFollowUpResultCaptureBuilder;
import com.automate.vehicleservices.entity.enums.*;
import com.automate.vehicleservices.event.FollowUpResultEvent;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.ServiceReminderFollowUpRepository;
import com.automate.vehicleservices.service.dto.ServiceReminderDTO;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import com.automate.vehicleservices.service.dto.ServiceReminderFollowUpActivityDTO;
import com.automate.vehicleservices.service.dto.ServiceReminderFollowUpDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Component
@Slf4j
public class ServiceReminderFollowUpService extends AbstractService {

    public static final String CUSTOMER_CARE_MANAGER = "Customer Care Manager";
    private final ServiceReminderFollowUpRepository serviceReminderFollowUpRepository;
    private final CrudService crudService;
    private final EventPublisher eventPublisher;
    private final EmployeeAllocationService employeeAllocationService;
    private final FollowUpClosingSynchronousActionService followUpClosingSynchronousActionService;
    private final EmployeeService employeeService;
    private final OrgDataAllocationStrategyTypeService orgDataAllocationStrategyTypeService;
    private final RoundRobinDataAllocationStrategyService roundRobinDataAllocationStrategyService;

    public ServiceReminderFollowUpService(ServiceReminderFollowUpRepository serviceReminderFollowUpRepository,
                                          CrudService crudService, EventPublisher eventPublisher,
                                          EmployeeAllocationService employeeAllocationService,
                                          FollowUpClosingSynchronousActionService followUpClosingSynchronousActionService, EmployeeService employeeService, OrgDataAllocationStrategyTypeService orgDataAllocationStrategyTypeService, RoundRobinDataAllocationStrategyService roundRobinDataAllocationStrategyService) {
        this.serviceReminderFollowUpRepository = serviceReminderFollowUpRepository;
        this.crudService = crudService;
        this.eventPublisher = eventPublisher;
        this.employeeAllocationService = employeeAllocationService;
        this.followUpClosingSynchronousActionService = followUpClosingSynchronousActionService;
        this.employeeService = employeeService;
        this.orgDataAllocationStrategyTypeService = orgDataAllocationStrategyTypeService;
        this.roundRobinDataAllocationStrategyService = roundRobinDataAllocationStrategyService;
    }

    @Transactional
    public void createFollowup(ServiceReminderDTO serviceReminderDTO) {

        final var serviceReminderDetails = serviceReminderDTO.getServiceReminderDetails();
        if (CollectionUtils.isEmpty(serviceReminderDetails))
            return;

        final var offlineReminders = serviceReminderDetails.stream()
                .filter(detail -> CommunicationModeEnum.OFFLINE == detail.getCommunicationMode().getType()).collect(
                        Collectors.toList());

        createFollowUpsForOfflineReminders(offlineReminders, serviceReminderDTO);
    }

    @Transactional
    public void createFollowupV1(ServiceReminderDTO serviceReminderDTO) {

        final var serviceReminderDetails = serviceReminderDTO.getServiceReminderDetails();
        if (CollectionUtils.isEmpty(serviceReminderDetails))
            return;

        final var offlineReminders = serviceReminderDetails.stream()
                .filter(detail -> Objects.isNull(detail.getCommunicationMode()) || Objects.isNull(detail.getCommunicationMode().getType())).collect(
                        Collectors.toList());

        createFollowUpsForOfflineReminders(offlineReminders, serviceReminderDTO);
    }

    private void createFollowUpsForOfflineReminders(List<ServiceReminderDetailsDTO> offlineReminders,
                                                    ServiceReminderDTO serviceReminderDTO) {

        final var serviceReminderFollowUps = offlineReminders.stream()
                .map(offlineReminder -> getServiceReminderFollowUp(serviceReminderDTO, offlineReminder))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(serviceReminderFollowUps))
            crudService.saveAll(serviceReminderFollowUps);
    }

    private ServiceReminderFollowUp getServiceReminderFollowUp(ServiceReminderDTO serviceReminderDTO,
                                                               ServiceReminderDetailsDTO offlineReminder) {
        final var serviceVehicleDTO = serviceReminderDTO.getServiceVehicle();
        final var serviceVehicleEntityOpt = crudService.findById(serviceVehicleDTO.getId(), ServiceVehicle.class);

        ServiceVehicle serviceVehicle = null;
        if (serviceVehicleEntityOpt.isEmpty())
            return null;

        serviceVehicle = serviceVehicleEntityOpt.get();
        final var customer = serviceVehicle.getCustomer();

//        commented existing flow like based tenant get cre
//        final var assignedCRE = assignCRE(serviceReminderDTO.getTenant().getId());

        final var serviceReminderDetailsOptional = crudService
                .findById(offlineReminder.getId(), ServiceReminderDetails.class);
        if (serviceReminderDetailsOptional.isEmpty())
            return null;

        final var assignedCRE = assignCREV1(serviceVehicle.getMdTenant(), serviceReminderDTO);

        final var serviceReminderFollowUp = ServiceReminderFollowUpBuilder.aServiceReminderFollowUp()
                .withCustomer(customer)
                .withServiceVehicle(serviceVehicle)
                .withTenant(serviceVehicle.getMdTenant())
                .withCre(assignedCRE)
                .withStatus(FollowUpStatus.OPEN)
                .withServiceReminderDetails(serviceReminderDetailsOptional.get())
                .build();
        final var reminderFollowUpActivity = ServiceReminderFollowUpActivityBuilder
                .aServiceReminderFollowUpActivity()
                .withStartDate(offlineReminder.getDateOfReminder())
                .withEndDate(offlineReminder.getDateOfReminder())
                .withFollowUpActivityStatus(FollowUpActivityStatus.OPEN)
                .withCre(assignedCRE)
                .withFollowUpReason(FollowUpReason.FRESH_CALL)
                .build();
        serviceReminderFollowUp.addServiceFollowUpActivity(reminderFollowUpActivity);

        return serviceReminderFollowUp;
    }

    /**
     * CRE assigning logic.
     *
     * @return
     */
    private Employee assignCRE(final int tenant) {
        final var employeeRoundRobinAllocation = employeeAllocationService.fetchAssignableCRE(tenant);
        if (Objects.nonNull(employeeRoundRobinAllocation))
            return employeeRoundRobinAllocation.getEmployee();

        return null;
    }

    private Employee assignCREV1(final MdTenant tenant, ServiceReminderDTO serviceReminderDTO) {
        MdOrganization organization = tenant.getMdOrganization();
        Integer serviceTypeId = serviceReminderDTO.getServiceType().getId();
        final var activeAllocationType = orgDataAllocationStrategyTypeService.getActiveAllocation(organization);
        if (AllocationType.MANUAL.equals(activeAllocationType.getAllocationType()))
            return null;
        List<RoundRobinDataAllocationStrategy> robinDataAllocationStrategyList = roundRobinDataAllocationStrategyService.getCREDetailsBasedOnOrgIdAndStatusAndServiceType(organization.getId(), serviceTypeId);
        if (robinDataAllocationStrategyList.isEmpty())
            return null;
//        This logic writes currently based assuming 100% allotment to each person
        return robinDataAllocationStrategyList.get(0).getEmployee();
    }

    public ServiceReminderFollowUpActivityDTO createFollowupActivity(FollowUpRequest followUpRequest) {
        final var followUpOptional = crudService
                .findById(followUpRequest.getFollowUpId(), ServiceReminderFollowUp.class);

        if (!followUpOptional.isPresent())
            throw new VehicleServicesException("Follow Up doesnt exists");

        final var serviceReminderFollowUp = followUpOptional.get();
        final Optional<ServiceReminderFollowUpActivity> serviceReminderFollowUpActivity = serviceReminderFollowUp
                .getServiceFollowUpActivities()
                .stream().filter(activity -> activity.getFollowUpActivityStatus() == FollowUpActivityStatus.OPEN)
                .findAny();
        if (serviceReminderFollowUpActivity.isPresent())
            throw new VehicleServicesException("Please close existing activity before creating a new one.");

        Employee cre = getEmployee(followUpRequest.getAssignCRE(), serviceReminderFollowUp.getCre());
        serviceReminderFollowUp.setStatus(FollowUpStatus.IN_PROGRESS); // parent follow-up stays in progress.
        final var reminderFollowUpActivity = ServiceReminderFollowUpActivityBuilder
                .aServiceReminderFollowUpActivity()
                .withStartDate(followUpRequest.getStartDate())
                .withEndDate(followUpRequest.getEndDate())
                .withFollowUpActivityStatus(FollowUpActivityStatus.OPEN)
                .withFollowUpReason(followUpRequest.getFollowUpReason())
                .withCre(cre)
                .withCustomerRemarks(followUpRequest.getCustomerRemarks())
                .withCreRemarks(followUpRequest.getCreRemarks())
                .withServiceReminderFollowUp(serviceReminderFollowUp)
                .build();

        final var save = crudService.save(reminderFollowUpActivity);
        return new ServiceReminderFollowUpActivityDTO(save);
    }

    private Employee getEmployee(int creId, Employee assignedEmployee) {
        Employee cre = assignedEmployee;
        if (creId != 0) {
            final var employee = crudService.findById(creId, Employee.class);
            employee.ifPresent(emp -> employeeAllocationService.allocate(emp.getId()));
            return employee.orElseGet(() -> assignedEmployee);
        }
        return cre;
    }

    @Transactional
    public ServiceReminderFollowUpActivityDTO updateFollowupActivity(FollowUpUpdateRequest followUpUpdateRequest,
                                                                     int loggedInEmpId) {
        final var followUpActivityOptional = crudService
                .findById(followUpUpdateRequest.getId(), ServiceReminderFollowUpActivity.class);

        final var followUpResultCapture = followUpUpdateRequest.getFollowUpResultCapture();

        final var followUP = Objects.nonNull(followUpResultCapture) && Objects
                .nonNull(followUpResultCapture.getReason());

        if (followUpActivityOptional.isEmpty())
            throw new VehicleServicesException("Follow Up doesnt exists");

        ServiceReminderFollowUpActivity serviceReminderFollowUpActivity = followUpActivityOptional.get();

        if (creChanged(followUpUpdateRequest.getAssignCRE(), serviceReminderFollowUpActivity.getCre())) {
            validatedCREChange(loggedInEmpId);

            Employee cre = getEmployee(followUpUpdateRequest.getAssignCRE(),
                    serviceReminderFollowUpActivity.getCre());

            if (Objects.nonNull(cre)) {
                serviceReminderFollowUpActivity.setCre(cre);
            }
        }
        serviceReminderFollowUpActivity.setStartDate(followUpUpdateRequest.getStartDate());
        serviceReminderFollowUpActivity.setEndDate(followUpUpdateRequest.getEndDate());
        serviceReminderFollowUpActivity.setFollowUpActivityStatus(followUpUpdateRequest.getFollowUpActivityStatus());
        serviceReminderFollowUpActivity.setFollowUpActivityResult(followUpUpdateRequest.getFollowUpActivityResult());
        serviceReminderFollowUpActivity.setCustomerRemarks(followUpUpdateRequest.getCustomerRemarks());
        serviceReminderFollowUpActivity.setCreRemarks(followUpUpdateRequest.getCreRemarks());
        serviceReminderFollowUpActivity.setFollowUpDate(followUpUpdateRequest.getFollowUpDate());
        serviceReminderFollowUpActivity.setFollowUpStatus(followUpUpdateRequest.getStatus());

        if (followUP) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonString = objectMapper.writeValueAsString(followUpResultCapture);
                serviceReminderFollowUpActivity.addServiceFollowUpResultCapture(
                        ServiceReminderFollowUpResultCaptureBuilder.aServiceReminderFollowUpResultCapture()
                                .withResultJsonData(jsonString).build());
            } catch (JsonProcessingException e) {
                log.error("Unable to capture result json", e);
            }
        }
        if (followUpUpdateRequest.getFollowUpActivityStatus() == FollowUpActivityStatus.CLOSED)
            serviceReminderFollowUpActivity.getServiceReminderFollowUp().setStatus(FollowUpStatus.CLOSED);

        final var save = crudService.save(serviceReminderFollowUpActivity);
        final var serviceReminderFollowUpActivityDTO = new ServiceReminderFollowUpActivityDTO(save);

        handleFollowUpClosingAction(followUpResultCapture, followUP, serviceReminderFollowUpActivityDTO);
        return serviceReminderFollowUpActivityDTO;
    }

    private void validatedCREChange(int loggedInEmpId) {
        log.info(String.format("Logged in employee : %d", loggedInEmpId));
        final var employee = employeeService.findEmployeesByMasterIdentifier(loggedInEmpId);

        log.info(String.format("Logged in employee - Services : ", null != employee ? employee.toString() :
                "No matching employee exists in the system."));

        if (Objects.isNull(employee))
            throw new VehicleServicesException(String.format("No matching employee exists in the services system for " +
                    "the logged in user with id %d", loggedInEmpId));

        if (!StringUtils.equalsIgnoreCase(employee.getRole(), CUSTOMER_CARE_MANAGER))
            throw new VehicleServicesException("Only CCM can change the assignee.");
    }

    private void handleFollowUpClosingAction(FollowUpResultCapture followUpResultCapture, boolean followUP,
                                             ServiceReminderFollowUpActivityDTO serviceReminderFollowUpActivityDTO) {
        Object followUpActionResponse = null;
        if (followUP) {
            if (followUpResultCapture.isAsync()) {
                final var followUpResultEvent = FollowUpResultEvent.builder()
                        .followUpResultCapture(followUpResultCapture)
                        .serviceReminderFollowUpActivityDTO(serviceReminderFollowUpActivityDTO).build();
                eventPublisher.publish(followUpResultEvent);
            } else {
                followUpActionResponse = followUpClosingSynchronousActionService
                        .handleSynchronousFollowUpAction(followUpResultCapture,
                                serviceReminderFollowUpActivityDTO);
            }
        }
        serviceReminderFollowUpActivityDTO.setFollowUpActionResponse(followUpActionResponse);
    }

    private boolean creChanged(int assignCRE, Employee cre) {
        return assignCRE != (null != cre ? cre.getId() : 0);
    }

    public List<?> masterData(FollowUpMasterDataType type) {
        switch (type) {
            case FOLLOW_UP_REASON:
                return Arrays.asList(FollowUpReason.values());
            case FOLLOW_UP_RESULT:
                return Arrays.asList(FollowUpActivityResult.values());
            case FOLLOW_UP_STATUS:
                return Arrays.asList(FollowUpActivityStatus.values());
            case FOLLOWUP_CLOSING_ACTION:
                final var customerInterestedReasons = CustomerInterestedReasons.values();
                final var customerNotInterestedReasons = CustomerNotInterestedReasons.values();
                return List.of(Map.of(CustomerInterestedReasons.class.getSimpleName(), customerInterestedReasons),
                        Map.of(CustomerNotInterestedReasons.class.getSimpleName(), customerNotInterestedReasons));
        }
        throw new VehicleServicesException(String.format("Invalid master data type requested: %s",
                type));

    }

    public List<ServiceReminderFollowUpDTO> activeFollowUpsByVehicle(String regNumber) {
        final var reminderFollowUps = serviceReminderFollowUpRepository
                .findByServiceVehicle_RegNumberAndStatusIn(regNumber, FollowUpStatus.OPEN,
                        FollowUpStatus.IN_PROGRESS);
        if (CollectionUtils.isEmpty(reminderFollowUps))
            return Collections.emptyList();
        return reminderFollowUps.stream().map(ServiceReminderFollowUpDTO::new).collect(Collectors.toList());
    }

    public List<ServiceReminderFollowUpDTO> allActiveFollowUpsByTenant(final String tenant) {
        final var reminderFollowUps = serviceReminderFollowUpRepository
                .findByTenant_TenantIdentifierAndStatusIn(tenant, FollowUpStatus.OPEN, FollowUpStatus.IN_PROGRESS);
        if (CollectionUtils.isEmpty(reminderFollowUps))
            return Collections.emptyList();
        return reminderFollowUps.stream().map(ServiceReminderFollowUpDTO::new).collect(Collectors.toList());
    }

    public ServiceReminderFollowUpDTO followUpActivitiesByParentFollowUpId(String tenant, int id) {
        final var reminderFollowUp = serviceReminderFollowUpRepository
                .findByIdAndTenant_TenantIdentifier(id, tenant);
        if (Objects.isNull(reminderFollowUp))
            throw new VehicleServicesException(HttpStatus.NOT_FOUND);
        return new ServiceReminderFollowUpDTO(reminderFollowUp);
    }
}