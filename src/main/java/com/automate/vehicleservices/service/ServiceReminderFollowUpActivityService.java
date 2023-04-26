package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.LeadAllocationDetails;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.RoDataDetails;
import com.automate.vehicleservices.entity.ServiceAppointment;
import com.automate.vehicleservices.entity.ServiceReminder;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleServiceSchedule;
import com.automate.vehicleservices.entity.builder.VehicleServiceHistoryBuilder;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;
import com.automate.vehicleservices.entity.enums.FollowUpCategory;
import com.automate.vehicleservices.entity.enums.FollowUpReason;
import com.automate.vehicleservices.entity.enums.FollowUpStepStatus;
import com.automate.vehicleservices.entity.enums.ReminderStatus;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;
import com.automate.vehicleservices.entity.specifications.FollowUpActivitySpecifications;
import com.automate.vehicleservices.event.UpdateVehicleEvent;
import com.automate.vehicleservices.event.VehicleEventData;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.LeadAllocationDetailRepository;
import com.automate.vehicleservices.repository.ServiceReminderFollowUpActivityRepository;
import com.automate.vehicleservices.repository.VehicleServiceScheduleRepository;
import com.automate.vehicleservices.repository.dtoprojection.CreLocationProjection;
import com.automate.vehicleservices.repository.dtoprojection.LeadAllocationCREProjection;
import com.automate.vehicleservices.service.dto.CreDashboardDTO;
import com.automate.vehicleservices.service.dto.EmployeeDTO;
import com.automate.vehicleservices.service.dto.FollowUpStats;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import com.automate.vehicleservices.service.dto.ServiceReminderFollowUpActivityDTO;
import com.automate.vehicleservices.service.dto.TargetConfDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ServiceReminderFollowUpActivityService extends AbstractService {

    private final ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository;
    private final HRMSIntegrationService hrmsIntegrationService;
    private final EmployeeService employeeService;
    private final FollowUpActivitySpecifications followUpActivitySpecifications;
    private final ServiceLogicReminderService serviceLogicReminderService;
    private final EventPublisher eventPublisher;
    private final TargetConfService targetConfService;
    private final LeadAllocationDetailRepository leadAllocationDetailRepository;

    public ServiceReminderFollowUpActivityService(
            ServiceReminderFollowUpActivityRepository serviceReminderFollowUpActivityRepository,
            HRMSIntegrationService hrmsIntegrationService, EmployeeService employeeService,
            FollowUpActivitySpecifications followUpActivitySpecifications, ServiceLogicReminderService serviceLogicReminderService, EventPublisher eventPublisher, TargetConfService targetConfService, LeadAllocationDetailRepository leadAllocationDetailRepository) {
        this.serviceReminderFollowUpActivityRepository = serviceReminderFollowUpActivityRepository;
        this.hrmsIntegrationService = hrmsIntegrationService;
        this.employeeService = employeeService;
        this.followUpActivitySpecifications = followUpActivitySpecifications;
        this.serviceLogicReminderService = serviceLogicReminderService;
        this.eventPublisher = eventPublisher;
        this.targetConfService = targetConfService;
        this.leadAllocationDetailRepository = leadAllocationDetailRepository;
    }

    public List<ServiceReminderFollowUpActivityDTO> followUpActivitiesByTenantReason(String tenant,
                                                                                     FollowUpReason followUpReason) {
        final var serviceReminderFollowUpActivities =
                serviceReminderFollowUpActivityRepository
                        .findByFollowUpReasonAndServiceReminderFollowUp_Tenant_TenantIdentifier(followUpReason, tenant);
        return getServiceReminderFollowUpActivityDTOS(serviceReminderFollowUpActivities);
    }

    private List<ServiceReminderFollowUpActivityDTO> getServiceReminderFollowUpActivityDTOS(
            List<ServiceReminderFollowUpActivity> serviceReminderFollowUpActivities) {
        if (CollectionUtils.isEmpty(serviceReminderFollowUpActivities))
            return Collections.emptyList();

        return serviceReminderFollowUpActivities.stream().map(ServiceReminderFollowUpActivityDTO::new).collect(
                Collectors.toList());
    }

    public ServiceReminderFollowUpActivityDTO followUpActivitiesByParentFollowUpIdAndActivityId(String tenant,
                                                                                                int followUpId,
                                                                                                int activityId) {
        final var reminderFollowUpActivity =
                serviceReminderFollowUpActivityRepository
                        .findByIdAndServiceReminderFollowUp_IdAndServiceReminderFollowUp_Tenant_TenantIdentifier(
                                activityId,
                                followUpId, tenant);
        if (Objects.isNull(reminderFollowUpActivity))
            throw new VehicleServicesException(HttpStatus.NOT_FOUND);
        return new ServiceReminderFollowUpActivityDTO(reminderFollowUpActivity);
    }

    /**
     * Fetches follow-ups for the given tenant and category, along with filters if any.
     *
     * @param tenant
     * @param loggedInEmpId
     * @param category
     * @param page
     * @param pageSize
     * @param serviceId
     * @param fromDate
     * @param toDate
     * @param vehicleModel
     * @return
     */
    public PaginatedSearchResponse followUpsByTenantAndLoggedInUserAndCategory(String tenant,
                                                                               int loggedInEmpId,
                                                                               FollowUpCategory category, int page,
                                                                               int pageSize, final Integer serviceId,
                                                                               final LocalDate fromDate,
                                                                               final LocalDate toDate,
                                                                               final String vehicleModel) {


        final List<Integer> inHouseEmployeeIds = inHouseEmployeeIds(loggedInEmpId);
        log.info(String.format("Fetching follow ups for employees: %s", inHouseEmployeeIds));
        Specification<ServiceReminderFollowUpActivity> specification = null;
        switch (category) {
            case FRESH_CALLS:
                specification = freshCallSpecification(inHouseEmployeeIds);
                break;
            case FUTURE_FOLLOW_UP:
                specification = futureFollowUpSpecification(inHouseEmployeeIds);
                break;
            case PENDING_FOLLOW_UP:
                specification = pendingFollowUpSpecification(inHouseEmployeeIds);
                break;
            default:
                throw new VehicleServicesException(String.format("Invalid Category %s", category));
        }
        specification = followupFilterSpecifications(serviceId, fromDate, toDate, vehicleModel, specification);

        Page<ServiceReminderFollowUpActivity> paginatedResults = serviceReminderFollowUpActivityRepository
                .findAll(specification, PageRequest.of(page, pageSize));

        final List<ServiceReminderFollowUpActivityDTO> serviceReminderFollowUpActivityDTOS =
                getServiceReminderFollowUpActivityDTOS(paginatedResults.getContent());
        return new PaginatedSearchResponse(paginatedResults, serviceReminderFollowUpActivityDTOS);
    }

    /**
     * Fetches follow-ups for the given tenant and category, along with filters if any.
     *
     * @param tenant
     * @param loggedInEmpId
     * @param category
     * @param page
     * @param pageSize
     * @param serviceId
     * @param fromDate
     * @param toDate
     * @param vehicleModel
     * @param categoryId
     * @param location
     * @param serviceCenterCode
     * @return
     */
    @Transactional
    public PaginatedSearchResponse followUpDetails(String tenant,
                                                   int loggedInEmpId,
                                                   FollowUpCategory category, int page,
                                                   int pageSize, final Integer serviceId,
                                                   final LocalDate fromDate,
                                                   final LocalDate toDate,
                                                   final String vehicleModel,
                                                   final MdOrganization organization, Integer categoryId, String location, String serviceCenterCode) {


        final List<Integer> inHouseEmployeeIds = inHouseEmployeeIds(loggedInEmpId);
        log.info(String.format("Fetching follow ups for employees: %s", inHouseEmployeeIds));
        Specification<ServiceReminderFollowUpActivity> specification = null;
        switch (category) {
            case TODAY:
                specification = todayCallSpecification(inHouseEmployeeIds);
                break;
            case PENDING:
                specification = pendingCallSpecification(inHouseEmployeeIds);
                break;
            case RESCHEDULE:
                specification = rescheduleCallSpecification(inHouseEmployeeIds);
                break;
            case BOOKED:
                specification = bookCallSpecification(inHouseEmployeeIds);
                break;
            case FUTURE_FOLLOW_UP:
                specification = afterUploadROFeatureFollowUpCallSpecification(inHouseEmployeeIds);
                break;
            default:
                throw new VehicleServicesException(String.format("Invalid Category %s", category));
        }
        specification = followupFilterSpecificationsV1(serviceId, fromDate, toDate, vehicleModel, specification,categoryId,location,serviceCenterCode);

        Page<ServiceReminderFollowUpActivity> results = serviceReminderFollowUpActivityRepository
                .findAll(specification, PageRequest.of(page, pageSize));

        final List<ServiceReminderFollowUpActivityDTO> serviceReminderFollowUpActivityDTOS =
                getServiceReminderFollowUpActivityDTOS(results.getContent());
        return new PaginatedSearchResponse(results, serviceReminderFollowUpActivityDTOS);
    }

    @Transactional
    public void updateBookedVehicleRoData(RoDataDetails roDataDetails, Integer tenant) {
        Optional<ServiceReminderFollowUpActivity> serviceReminderFollowUpActivity = serviceReminderFollowUpActivityRepository.getBookedActiveRecordsBasedOnVinAndServiceType(roDataDetails.getVin(), roDataDetails.getServiceType(), tenant);
        if (serviceReminderFollowUpActivity.isPresent()) {
            ServiceReminderFollowUpActivity activity = serviceReminderFollowUpActivity.get();
            activity.setRoData(roDataDetails);
            serviceReminderFollowUpActivityRepository.save(activity);

            log.info(activity.getServiceReminderFollowUp().getId()+"::::::");
            log.info(activity.getServiceReminderFollowUp().getServiceReminderDetails().getId()+"::::::");
            log.info(activity.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder().getId()+"::::::");


            ServiceReminder reminder = activity.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder();
            reminder.setActive(false);
            reminder.setReminderStatus(ReminderStatus.COMPLETED);
            crudService.save(reminder);

            VehicleServiceSchedule serviceSchedule = activity.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder().getVehicleServiceSchedule();
            serviceSchedule.setStatus(ScheduleStatus.COMPLETED);
            crudService.save(serviceSchedule);

            ServiceVehicle vehicle = activity.getServiceReminderFollowUp().getServiceVehicle();
            Optional<LeadAllocationDetails> leadAllocationDetails =  vehicle.getLeadAllocationDetails().stream().filter(data->data.getStatus().equals(ActiveInActiveStatus.ACTIVE)).findFirst();
            Optional<ServiceAppointment> appointments =  vehicle.getAppointments().stream().filter(data->data.getStatus().equals(ServiceAppointmentStatus.BOOKED)).findFirst();

            final var vehicleServiceHistory = VehicleServiceHistoryBuilder.aVehicleServiceHistory()
                    .withServiceManager(roDataDetails.getServiceAdvisor())
                    .withKmReading(vehicle.getCurrentKmReading())
                    .withServiceCenter(roDataDetails.getServiceCenterLocation())
                    .withServiceAmount(roDataDetails.getTotalBillAmount().floatValue())
                    .withMdServiceType(activity.getServiceReminderFollowUp().getServiceReminderDetails().getServiceReminder().getMdServiceType())
                    .withServiceVehicle(vehicle)
                    .withMdTenant(vehicle.getMdTenant())
                    .build();
            vehicleServiceHistory.setRoData(roDataDetails);

            if(leadAllocationDetails.isPresent()){
                vehicleServiceHistory.setDealerName(leadAllocationDetails.get().getCreDetail().getName());
                vehicleServiceHistory.setDealerLocation(leadAllocationDetails.get().getCreLocation());
                vehicleServiceHistory.setServiceLocation(leadAllocationDetails.get().getCreLocation());
            }

            if(appointments.isPresent()){
                vehicleServiceHistory.setServiceDate(appointments.get().getServiceDate());
                ServiceAppointment appointment = appointments.get();
                appointment.setStatus(ServiceAppointmentStatus.COMPLETED);
                crudService.save(appointment);
            }

            crudService.save(vehicleServiceHistory);

            eventPublisher.publish(UpdateVehicleEvent.builder()
                    .eventData(new VehicleEventData(vehicle)).build());
        }
    }

    @Transactional
    public Object followUpStatsV1(int loggedInEmpId, MdOrganization organization) {
        Map<String, Object> responseMap = new HashMap<>();

        LocalDate currentDate = LocalDate.now();
        LocalDate currentMonthFirstDay = currentDate.withDayOfMonth(1);
        LocalDate currentMonthLastDay = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        final List<Integer> inHouseEmployeeIds =inHouseEmployeeIds(loggedInEmpId);

        List<TargetConfDTO> targetConfDTOList = targetConfService.fetchAllTargetConf(organization.getId());


        log.info(String.format("Fetching follow ups for employees: %s", inHouseEmployeeIds));
        Long today = serviceReminderFollowUpActivityRepository.count(todayCallSpecification(inHouseEmployeeIds));
        Long pending = serviceReminderFollowUpActivityRepository.count(pendingCallSpecification(inHouseEmployeeIds));
        Long attempted = serviceReminderFollowUpActivityRepository.count(followUpStatus(FollowUpStepStatus.ATTEMPTED, inHouseEmployeeIds,currentMonthFirstDay,currentMonthLastDay));
        Long contacted = serviceReminderFollowUpActivityRepository.count(followUpStatus(FollowUpStepStatus.CONTACTED, inHouseEmployeeIds,currentMonthFirstDay,currentMonthLastDay));
        Long booking = serviceReminderFollowUpActivityRepository.count(followUpStatus(FollowUpStepStatus.BOOKED, inHouseEmployeeIds,currentMonthFirstDay,currentMonthLastDay));
        List<CreLocationProjection> creLocationProjections = serviceReminderFollowUpActivityRepository.getCreAndCenterCode(inHouseEmployeeIds,
                List.of(FollowUpStepStatus.ATTEMPTED.name(),FollowUpStepStatus.BOOKED.name(),FollowUpStepStatus.CONTACTED.name()),
                currentMonthFirstDay,
                currentMonthLastDay);

        Map<String, List<CreLocationProjection>> mapLocation = creLocationProjections.stream().collect(Collectors.groupingBy(s -> s.getCenterCode()));
        Map<String, Map<String, Integer>> innerMap = new HashMap<>();
        targetConfDTOList.stream().forEach(data -> {
            if (Objects.nonNull(mapLocation.get(data.getDealerId()))) {
                List<CreLocationProjection> collect =  mapLocation.get(data.getDealerId());
                data.getTargetConfParameter().stream().forEach(res -> {
                    Map<String, Integer> childMap = new HashMap<>();
                    int target = res.getTarget() == 0 ? 1 : res.getTarget() * (collect.size() == 0 ? 1 : collect.size());
                    if (res.getParameterName().equalsIgnoreCase("attempt")) {
                        getTarget(attempted, innerMap, childMap, target , "attempt");
                    }

                    if (res.getParameterName().equalsIgnoreCase("schedule")) {
                        getTarget(booking, innerMap, childMap, target , "schedule");
                    }

                    if (res.getParameterName().equalsIgnoreCase("contact")) {
                        getTarget(contacted, innerMap, childMap, target , "contact");
                    }
                });
            }
        });

        responseMap.put("today", today);
        responseMap.put("pending", pending);
        responseMap.put("attempted", attempted);
        responseMap.put("contacted", contacted);
        responseMap.put("booking", booking);
        responseMap.put("other",innerMap);
        return responseMap;
    }

    private void getTarget(Long attempted, Map<String, Map<String, Integer>> innerMap, Map<String, Integer> childMap, int target , String key) {
        if(Objects.nonNull(innerMap.get(key))){
            target = target + innerMap.get(key).get("Target");
        }
        childMap.put("Target", target);
        childMap.put("Ach", attempted.intValue());
        childMap.put("Ach%", Math.round(attempted.intValue()/ target)*100);
        innerMap.put(key, childMap);
    }

    public Object dashboardData(String dealerCode, String location, LocalDate from, LocalDate to, Integer creId, Integer loggedInEmpId, MdOrganization org) {
        LocalDate currentDate = LocalDate.now();

        LocalDate currentMonthFirstDay;
        LocalDate currentMonthLastDay;
        if (Objects.nonNull(from) && Objects.nonNull(to)) {
            currentMonthFirstDay = from;
            currentMonthLastDay = to;
        }else{
            currentMonthFirstDay = currentDate.withDayOfMonth(1);
            currentMonthLastDay = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        }

        final List<Integer> inHouseEmployeeIds;
        if (Objects.nonNull(creId)) {
            inHouseEmployeeIds = Collections.singletonList(creId);
        } else {
            inHouseEmployeeIds = inHouseEmployeeIds(loggedInEmpId);
        }

        List<LeadAllocationCREProjection> leadAllocationCREProjections = leadAllocationDetailRepository.getCreLeadDetails(inHouseEmployeeIds,dealerCode,location);

        var creDashboardDTOList = leadAllocationCREProjections.stream().map(data -> {
            Long attempted = serviceReminderFollowUpActivityRepository.count(followUpStatus(FollowUpStepStatus.ATTEMPTED, Collections.singletonList(data.getCreId()), currentMonthFirstDay, currentMonthLastDay));
            Long contacted = serviceReminderFollowUpActivityRepository.count(followUpStatus(FollowUpStepStatus.CONTACTED, Collections.singletonList(data.getCreId()), currentMonthFirstDay, currentMonthLastDay));
            Long booking = serviceReminderFollowUpActivityRepository.count(followUpStatus(FollowUpStepStatus.BOOKED, Collections.singletonList(data.getCreId()), currentMonthFirstDay, currentMonthLastDay));
            TargetConfDTO targetConfDTO = targetConfService.fetchTargetConfBasedDealerCode(data.getDealerCode(), org.getId());
            AtomicReference<Integer> attemptTarget = new AtomicReference<>(0);
            AtomicReference<Integer> scheduleTarget = new AtomicReference<>(0);
            AtomicReference<Integer> contactTarget = new AtomicReference<>(0);
            targetConfDTO.getTargetConfParameter().stream().forEach(para -> {
                if (para.getParameterName().equalsIgnoreCase("attempt")) {
                    attemptTarget.set(para.getTarget());
                } else if (para.getParameterName().equalsIgnoreCase("schedule")) {
                    scheduleTarget.set(para.getTarget());
                } else if (para.getParameterName().equalsIgnoreCase("contact")) {
                    contactTarget.set(para.getTarget());
                }
            });

            return new CreDashboardDTO(
                    data.getCreName(),
                    data.getLeadCount(),
                    attempted,
                    attemptTarget.get(),
                    contacted,
                    contactTarget.get(),
                    booking,
                    scheduleTarget.get(),
                    0,
                    0);
        }).collect(Collectors.toList());

        var totalAttempt = creDashboardDTOList.stream().map(CreDashboardDTO::getAttemptAchieved).reduce(0l,Long::sum);
        var totalContact = creDashboardDTOList.stream().map(CreDashboardDTO::getContactAchieved).reduce(0l,Long::sum);
        var totalBooked = creDashboardDTOList.stream().map(CreDashboardDTO::getBookAchieved).reduce(0l,Long::sum);

        var totalAttemptTarget = creDashboardDTOList.stream().map(CreDashboardDTO::getAttemptTarget).reduce(0,Integer::sum);
        var totalContactTarget = creDashboardDTOList.stream().map(CreDashboardDTO::getContactTarget).reduce(0,Integer::sum);
        var totalBookedTarget = creDashboardDTOList.stream().map(CreDashboardDTO::getBookTarget).reduce(0,Integer::sum);

        totalAttemptTarget = totalAttemptTarget == 0 ? 1 : totalAttemptTarget;
        totalContactTarget = totalContactTarget == 0 ? 1 : totalContactTarget;
        totalBookedTarget = totalBookedTarget == 0 ? 1 : totalBookedTarget;
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("data", creDashboardDTOList);
        responseMap.put("totalAttempt", totalAttempt);
        responseMap.put("totalContact", totalContact);
        responseMap.put("totalBooked", totalBooked);
        responseMap.put("totalReport", 0);
        responseMap.put("totalReportPercentage", 0);
        responseMap.put("totalAttemptPercentage", Math.round(totalAttempt / totalAttemptTarget) * 100);
        responseMap.put("totalContactPercentage", Math.round(totalContact / totalContactTarget) * 100);
        responseMap.put("totalBookedPercentage", Math.round(totalBooked / totalBookedTarget) * 100);

        return responseMap;
    }

    private List<Integer> inHouseEmployeeIds(int loggedInEmpId) {
        List<Integer> masterIdentifiers = new ArrayList<>();
        masterIdentifiers.add(loggedInEmpId);

        masterIdentifiers.addAll(hrmsIntegrationService.fetchAllEmployeesByManager(loggedInEmpId));
        return fetchEmployeeIdsByMasterIdentifier(masterIdentifiers);
    }

    private Specification<ServiceReminderFollowUpActivity> followupFilterSpecifications(final Integer serviceId,
                                                                                        final LocalDate fromDate,
                                                                                        final LocalDate toDate,
                                                                                        final String vehicleModel,
                                                                                        Specification<ServiceReminderFollowUpActivity> specification) {

        return getServiceReminderFollowUpActivitySpecification(serviceId, fromDate, toDate, vehicleModel, specification);

    }

    private Specification<ServiceReminderFollowUpActivity> followupFilterSpecificationsV1(final Integer serviceId,
                                                                                          final LocalDate fromDate,
                                                                                          final LocalDate toDate,
                                                                                          final String vehicleModel,
                                                                                          Specification<ServiceReminderFollowUpActivity> specification,
                                                                                          Integer categoryId, String location, String serviceCenterCode) {

        specification = getServiceReminderFollowUpActivitySpecification(serviceId, fromDate, toDate, vehicleModel, specification);
        if (null != categoryId && categoryId > 0)
            specification = specification.and(followUpActivitySpecifications.categoryId(categoryId));
        if (StringUtils.isNotBlank(location))
            specification = specification.and(followUpActivitySpecifications.locationEqual(location));
        if (StringUtils.isNotBlank(serviceCenterCode))
            specification = specification.and(followUpActivitySpecifications.serviceCenterCodeEqual(serviceCenterCode));
        return specification;
    }

    private Specification<ServiceReminderFollowUpActivity> getServiceReminderFollowUpActivitySpecification(Integer serviceId, LocalDate fromDate, LocalDate toDate, String vehicleModel, Specification<ServiceReminderFollowUpActivity> specification) {
        if (StringUtils.isNotBlank(vehicleModel))
            specification = specification
                    .and(followUpActivitySpecifications.vehicleModel(vehicleModel));
        if (Objects.nonNull(fromDate) && Objects.nonNull(toDate))
            specification = specification
                    .and(followUpActivitySpecifications.startDateGreaterThanEqual(fromDate))
                    .and(followUpActivitySpecifications.endDateLessThanEqual(toDate));

        if (null != serviceId && serviceId > 0)
            specification = specification.and(followUpActivitySpecifications.serviceTypeIdEquals(serviceId));
        return specification;
    }

    private Specification<ServiceReminderFollowUpActivity> pendingFollowUpSpecification(
            List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonNotEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds))
                .and(followUpActivitySpecifications.endDateLessThanEqual(LocalDate.now()));
    }

    private Specification<ServiceReminderFollowUpActivity> futureFollowUpSpecification(
            List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonNotEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds))
                .and(followUpActivitySpecifications.endDateGreaterThan(LocalDate.now()));
    }

    private Specification<ServiceReminderFollowUpActivity> freshCallSpecification(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> todayCallSpecification(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL)) // date compare
                .and(followUpActivitySpecifications.checkedVehicleScheduleStatus(ScheduleStatus.IN_PROGRESS))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.endDateEquals(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> pendingCallSpecification(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.checkedVehicleScheduleStatus(ScheduleStatus.IN_PROGRESS))
                .and(followUpActivitySpecifications.followUpStatusIsNull())
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.endDateLessThanEqual(LocalDate.now()))  // date compare
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> rescheduleCallSpecification(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.RESCHEDULE))
                .or(followUpActivitySpecifications.reasonEquals(FollowUpReason.CUSTOMER_REQUESTED_CALL_BACK))
                .and(followUpActivitySpecifications.checkedVehicleScheduleStatus(ScheduleStatus.IN_PROGRESS))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> bookCallSpecification(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.resultEquals(FollowUpActivityResult.SERVICE_BOOKED))
                .or(followUpActivitySpecifications.followUpStatusEquals(FollowUpStepStatus.BOOKED))
                .and(followUpActivitySpecifications.checkedVehicleScheduleStatus(ScheduleStatus.IN_PROGRESS))
                .and(followUpActivitySpecifications.closedStatus())
                .and((root, criteriaQuery, cb) -> cb.isNull(root.get("roData")))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> afterUploadROFeatureFollowUpCallSpecification(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.resultEquals(FollowUpActivityResult.SERVICE_BOOKED))
                .and((root, criteriaQuery, cb) -> cb.isNotNull(root.get("roData")))
                .and(followUpActivitySpecifications.closedStatus())
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> followUpStatus(FollowUpStepStatus status, List<Integer> inHouseEmployeeIds, LocalDate from, LocalDate to) {
        return Specification
                .where(followUpActivitySpecifications.followUpStatusEquals(status))
                .and(followUpActivitySpecifications.endDateBetween(from,to))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private List<Integer> fetchEmployeeIdsByMasterIdentifier(List<Integer> masterIdentifiers) {
        final var employeeDTOS = employeeService.findEmployeesByMasterIdentifier(masterIdentifiers);
        if (CollectionUtils.isEmpty(employeeDTOS))
            return Collections.emptyList();

        return employeeDTOS.stream().map(EmployeeDTO::getId).collect(Collectors.toList());
    }


    private List<Integer> fetchEmployeeIdsByMasterIdentifier(Integer... masterIdentifiers) {
        return fetchEmployeeIdsByMasterIdentifier(Arrays.asList(masterIdentifiers));
    }


    public FollowUpStats followUpStats(String tenant, int loggedInEmpId) {
        // Type of Call -Fresh Call, EndDate - today, Status - Open
        final List<Integer> inHouseEmployeeIds = inHouseEmployeeIds(loggedInEmpId);
        log.info(String.format("Fetching follow ups for employees: %s", inHouseEmployeeIds));

        long totalFreshCallsForToday =
                serviceReminderFollowUpActivityRepository.count(totalFreshCallsForToday(inHouseEmployeeIds));
        long futureFreshCalls = serviceReminderFollowUpActivityRepository.count(futureFreshCalls(inHouseEmployeeIds));
        long futureFollowUpCallsAsOfToday =
                serviceReminderFollowUpActivityRepository.count(futureFollowUpSpecification(inHouseEmployeeIds));
        long followUpsToBeClosedByToday =
                serviceReminderFollowUpActivityRepository.count(followUpsToBeClosedByToday(inHouseEmployeeIds));
        long pendingFollowUpCallsAsOfToday =
                serviceReminderFollowUpActivityRepository.count(pendingFollowUpCallsAsOfToday(inHouseEmployeeIds));
        long freshCallsMadeToday =
                serviceReminderFollowUpActivityRepository.count(freshCallsMadeToday(inHouseEmployeeIds));
        long followUpCallsMadeToday =
                serviceReminderFollowUpActivityRepository.count(followUpCallsMadeToday(inHouseEmployeeIds));
        long freshCallsMadeLastMonth =
                serviceReminderFollowUpActivityRepository.count(freshCallsMadeLastMonth(inHouseEmployeeIds));
        long totalFreshCallsLastMonth =
                serviceReminderFollowUpActivityRepository.count(totalFreshCallsLastMonth(inHouseEmployeeIds));
        long followUpCallsMadeLastMonth =
                serviceReminderFollowUpActivityRepository.count(followUpCallsMadeLastMonth(inHouseEmployeeIds));
        long totalFollowUpCallsLastMonth =
                serviceReminderFollowUpActivityRepository.count(totalFollowUpCallsLastMonth(inHouseEmployeeIds));
        long allCallsPendingAsOfToday =
                serviceReminderFollowUpActivityRepository.count(allCallsPendingAsOfToday(inHouseEmployeeIds));

        return FollowUpStats.builder()
                .totalFreshCallsForToady(totalFreshCallsForToday)
                .futureFreshCalls(futureFreshCalls)
                .futureFollowUpCallsAsOfToday(futureFollowUpCallsAsOfToday)
                .followUpCallsMadeToday(followUpCallsMadeToday)
                .followUpsToBeClosedByToday(followUpsToBeClosedByToday)
                .freshCallsMadeToday(freshCallsMadeToday)
                .pendingFollowUpCallsAsOfToday(pendingFollowUpCallsAsOfToday)
                .freshCallsMadeLastMonth(freshCallsMadeLastMonth)
                .totalFreshCallsLastMonth(totalFreshCallsLastMonth)
                .followUpCallsMadeLastMonth(followUpCallsMadeLastMonth)
                .totalFollowUpCallsLastMonth(totalFollowUpCallsLastMonth)
                .allCallsPendingAsOfToday(allCallsPendingAsOfToday)
                .build();
    }

    private Specification<ServiceReminderFollowUpActivity> totalFreshCallsForToday(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.endDateEquals(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> futureFreshCalls(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.startDateGreaterThan(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> followUpsToBeClosedByToday(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonNotEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.endDateEquals(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> pendingFollowUpCallsAsOfToday(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonNotEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.endDateLessThanEqual(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> allCallsPendingAsOfToday(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.openStatus())
                .and(followUpActivitySpecifications.endDateLessThanEqual(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> freshCallsMadeToday(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.closedStatus())
                .and(followUpActivitySpecifications.endDateEquals(LocalDate.now()))
                .and(followUpActivitySpecifications.lastModifiedDateEquals(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> followUpCallsMadeToday(List<Integer> inHouseEmployeeIds) {
        return Specification
                .where(followUpActivitySpecifications.reasonNotEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.closedStatus())
                .and(followUpActivitySpecifications.lastModifiedDateEquals(LocalDate.now()))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> freshCallsMadeLastMonth(List<Integer> inHouseEmployeeIds) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate lastMonthDateAdjustedToFirstDay = lastMonth.withDayOfMonth(1);
        LocalDate lastMonthDateAdjustedToLastDay = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.closedStatus())
                .and(followUpActivitySpecifications.startDateGreaterThanEqual(lastMonthDateAdjustedToFirstDay))
                .and(followUpActivitySpecifications.endDateLessThanEqual(lastMonthDateAdjustedToLastDay))
                .and(followUpActivitySpecifications.lastModifiedDateGreaterThanOrEquals(lastMonthDateAdjustedToFirstDay))
                .and(followUpActivitySpecifications.lastModifiedDateLessThanOrEquals(lastMonthDateAdjustedToLastDay))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> totalFreshCallsLastMonth(List<Integer> inHouseEmployeeIds) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate lastMonthDateAdjustedToFirstDay = lastMonth.withDayOfMonth(1);
        LocalDate lastMonthDateAdjustedToLastDay = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
        return Specification
                .where(followUpActivitySpecifications.reasonEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.startDateGreaterThanEqual(lastMonthDateAdjustedToFirstDay))
                .and(followUpActivitySpecifications.endDateLessThanEqual(lastMonthDateAdjustedToLastDay))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> totalFollowUpCallsLastMonth(List<Integer> inHouseEmployeeIds) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate lastMonthDateAdjustedToFirstDay = lastMonth.withDayOfMonth(1);
        LocalDate lastMonthDateAdjustedToLastDay = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
        return Specification
                .where(followUpActivitySpecifications.reasonNotEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.startDateGreaterThanEqual(lastMonthDateAdjustedToFirstDay))
                .and(followUpActivitySpecifications.endDateLessThanEqual(lastMonthDateAdjustedToLastDay))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

    private Specification<ServiceReminderFollowUpActivity> followUpCallsMadeLastMonth(List<Integer> inHouseEmployeeIds) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate lastMonthDateAdjustedToFirstDay = lastMonth.withDayOfMonth(1);
        LocalDate lastMonthDateAdjustedToLastDay = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
        return Specification
                .where(followUpActivitySpecifications.reasonNotEquals(FollowUpReason.FRESH_CALL))
                .and(followUpActivitySpecifications.closedStatus())
                .and(followUpActivitySpecifications.startDateGreaterThanEqual(lastMonthDateAdjustedToFirstDay))
                .and(followUpActivitySpecifications.endDateLessThanEqual(lastMonthDateAdjustedToLastDay))
                .and(followUpActivitySpecifications.lastModifiedDateGreaterThanOrEquals(lastMonthDateAdjustedToFirstDay))
                .and(followUpActivitySpecifications.lastModifiedDateLessThanOrEquals(lastMonthDateAdjustedToLastDay))
                .and(followUpActivitySpecifications.creIDsIn(inHouseEmployeeIds));
    }

}
