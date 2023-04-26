package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.builder.ServiceReminderBuilder;
import com.automate.vehicleservices.entity.builder.ServiceReminderDetailsBuilder;
import com.automate.vehicleservices.entity.enums.ReminderStatus;
import com.automate.vehicleservices.event.ReminderEntrySuccessEvent;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.dtoprojection.ServiceReminderPrefDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.service.MdServiceReminderPrefService;
import com.automate.vehicleservices.service.MdTenantService;
import com.automate.vehicleservices.service.ServiceLogicReminderService;
import com.automate.vehicleservices.service.ServiceVehicleService;
import com.automate.vehicleservices.service.VehicleServiceScheduleService;
import com.automate.vehicleservices.service.dto.ServiceReminderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class VehicleCoreReminderEngine {

    private final MdServiceReminderPrefService mdServiceReminderPrefService;
    private final CrudService crudService;
    private final MdTenantService tenantService;
    private final ServiceVehicleService serviceVehicleService;
    private final VehicleServiceScheduleService vehicleServiceScheduleService;
    private final EventPublisher eventPublisher;
    private final ServiceLogicReminderService serviceLogicReminderService;

    public VehicleCoreReminderEngine(MdServiceReminderPrefService mdServiceReminderPrefService,
                                     CrudService crudService, MdTenantService tenantService,
                                     ServiceVehicleService serviceVehicleService,
                                     VehicleServiceScheduleService vehicleServiceScheduleService,
                                     EventPublisher eventPublisher, ServiceLogicReminderService serviceLogicReminderService) {
        this.mdServiceReminderPrefService = mdServiceReminderPrefService;
        this.crudService = crudService;
        this.tenantService = tenantService;
        this.serviceVehicleService = serviceVehicleService;
        this.vehicleServiceScheduleService = vehicleServiceScheduleService;
        this.eventPublisher = eventPublisher;
        this.serviceLogicReminderService = serviceLogicReminderService;
    }

    /**
     * Initiates reminder flow for the given vehicle. This flow includes, based on service type fetching deriving the
     * start and end dates of reminders and persisting the same in service_reminder and service_reminder_details
     * tables.
     *
     * @param serviceScheduleDTO
     */
    @Transactional
    public void initiateReminderFlow(final ServiceScheduleDTO serviceScheduleDTO) {

        // Update vehicle schedule to in_progress
        vehicleServiceScheduleService.updateVehicleScheduleStatusNewToInProgress(serviceScheduleDTO
                .getServiceVehicle().getId());

        final var serviceType = serviceScheduleDTO.getServiceType();

        // fetch reminder Preferences by service type
        List<ServiceReminderPrefDTO> serviceReminderPreferences = fetchReminderPreferencesByServiceType(serviceType);

        final var serviceReminder = deriveServiceReminder(serviceReminderPreferences, serviceScheduleDTO);
        final var save = crudService.save(serviceReminder);

        ServiceReminderDTO serviceReminderDTO = new ServiceReminderDTO(save);
        eventPublisher.publish(ReminderEntrySuccessEvent.builder().eventData(serviceReminderDTO).build());
    }


    @Transactional
    public void initiateReminderFlowV1(final ServiceScheduleDTO serviceScheduleDTO) {

        // Update vehicle schedule to in_progress
        vehicleServiceScheduleService.updateVehicleScheduleStatusNewToInProgress(serviceScheduleDTO
                .getServiceVehicle().getId());

        final var serviceType = serviceScheduleDTO.getServiceType();

        // fetch reminder Preferences by service type
        ServiceLogicReminderDetails serviceLogicReminderDetails = fetchReminderDetailsByServiceType(serviceType);

        final var serviceReminder = deriveServiceReminderV1(serviceLogicReminderDetails, serviceScheduleDTO);
        final var save = crudService.save(serviceReminder);

        ServiceReminderDTO serviceReminderDTO = new ServiceReminderDTO(save);
        eventPublisher.publish(ReminderEntrySuccessEvent.builder().eventData(serviceReminderDTO).build());
    }

    /**
     * based on reminder preferences derives service reminder and it's child reminder details.
     *
     * @param serviceReminderPreferences
     * @param serviceScheduleDTO
     */
    private ServiceReminder deriveServiceReminder(List<ServiceReminderPrefDTO> serviceReminderPreferences,
                                                  final ServiceScheduleDTO serviceScheduleDTO) {
        if (Objects.isNull(serviceReminderPreferences) || serviceReminderPreferences.isEmpty())
            throw new VehicleServicesException(String.format("No reminder preferences available for the schedule %s",
                    serviceScheduleDTO.toString()));

        final var serviceDueDateRecommended = serviceScheduleDTO.getServiceDueDateRecommended();

        List<LocalDate> dateList = new ArrayList<>();
        List<ServiceReminderDetails> serviceReminderDetailsList = new ArrayList<>();

        final String regNumber = serviceScheduleDTO.getServiceVehicle().getRegNumber();
        final ServiceVehicle serviceVehicle = serviceVehicleService.findByVehicleRegNumber(regNumber);
        final Customer customer = serviceVehicle.getCustomer();
        for (ServiceReminderPrefDTO reminderPrefDTO : serviceReminderPreferences) {
            final var derivedDate = reminderDateBasedOnPreference(serviceDueDateRecommended, reminderPrefDTO);
            if (Objects.nonNull(derivedDate) && !derivedDate.isBefore(LocalDate.now()))
                dateList.add(derivedDate);

            serviceReminderDetailsList.add(ServiceReminderDetailsBuilder.aServiceReminderDetails()
                    .withDateOfReminder(derivedDate)
                    .withCommunicationMode(reminderPrefDTO.getCommunicationMode())
                    .withCommunicationAddress(communicationAddress(reminderPrefDTO.getCommunicationMode(), customer))
                    .withActive(true)
                    .build());
        }
        if (dateList.isEmpty())
            throw new VehicleServicesException(String.format("No reminder dates available on or after today %s",
                    serviceScheduleDTO.toString()));
        Collections.sort(dateList); // sort the dates for identifying start and end date

        final LocalDate reminderStartDate = dateList.get(0);
        final LocalDate reminderEndDate = dateList.get(dateList.size() - 1);

        final var serviceReminder = ServiceReminderBuilder.aServiceReminder().withActive(true).withMdTenant(null)
                .withReminderStatus(ReminderStatus.ACTIVE)
                .withReminderStartDate(reminderStartDate)
                .withRemindUntil(reminderEndDate)
                .withMdServiceType(crudService.findById(serviceScheduleDTO.getServiceType(),
                        MdServiceType.class).orElse(null))
                .withServiceVehicle(serviceVehicle)
                .withVehicleServiceSchedule(crudService.findById(serviceScheduleDTO.getId(),
                        VehicleServiceSchedule.class).orElse(null))
                .withMdTenant(tenantService.tenantByIdentifier(serviceScheduleDTO.getTenantIdentifier()))
                .build();

        for (ServiceReminderDetails serviceReminderDetails : serviceReminderDetailsList) {
            serviceReminder.addServiceReminderDetail(serviceReminderDetails);
        }

        return serviceReminder;

    }

    private ServiceReminder deriveServiceReminderV1(ServiceLogicReminderDetails serviceReminderPreferences,
                                                  final ServiceScheduleDTO serviceScheduleDTO) {
        if (Objects.isNull(serviceReminderPreferences))
            throw new VehicleServicesException(String.format("No reminder preferences available for the schedule %s",
                    serviceScheduleDTO.toString()));

        final var serviceDueDateRecommended = serviceScheduleDTO.getServiceDueDateRecommended();

        List<LocalDate> dateList = new ArrayList<>();
        List<ServiceReminderDetails> serviceReminderDetailsList = new ArrayList<>();

        final String regNumber = serviceScheduleDTO.getServiceVehicle().getRegNumber();
        final ServiceVehicle serviceVehicle = serviceVehicleService.findByVehicleRegNumber(regNumber);

        List<String> reminderDays = List.of(serviceReminderPreferences.getReminderDay().split(","));

        for (String days : reminderDays){
            final var derivedDate = serviceDueDateRecommended.minusDays(Integer.valueOf(days));
            if (Objects.nonNull(derivedDate) && !derivedDate.isBefore(LocalDate.now()))
                dateList.add(derivedDate);

            serviceReminderDetailsList.add(ServiceReminderDetailsBuilder.aServiceReminderDetails()
                    .withDateOfReminder(derivedDate)
                    .withActive(true)
                    .build());
        }

        if (dateList.isEmpty())
            throw new VehicleServicesException(String.format("No reminder dates available on or after today %s",
                    serviceScheduleDTO.toString()));
        Collections.sort(dateList); // sort the dates for identifying start and end date

        final LocalDate reminderStartDate = dateList.get(0);
        final LocalDate reminderEndDate = dateList.get(dateList.size() - 1);

        final var serviceReminder = ServiceReminderBuilder.aServiceReminder().withActive(true).withMdTenant(null)
                .withReminderStatus(ReminderStatus.ACTIVE)
                .withReminderStartDate(reminderStartDate)
                .withRemindUntil(reminderEndDate)
                .withMdServiceType(crudService.findById(serviceScheduleDTO.getServiceType(),
                        MdServiceType.class).orElse(null))
                .withServiceVehicle(serviceVehicle)
                .withVehicleServiceSchedule(crudService.findById(serviceScheduleDTO.getId(),
                        VehicleServiceSchedule.class).orElse(null))
                .withMdTenant(tenantService.tenantByIdentifier(serviceScheduleDTO.getTenantIdentifier()))
                .build();

        for (ServiceReminderDetails serviceReminderDetails : serviceReminderDetailsList) {
            serviceReminder.addServiceReminderDetail(serviceReminderDetails);
        }

        return serviceReminder;

    }

    private String communicationAddress(CommunicationMode communicationMode, Customer customer) {
        switch (communicationMode.getType()) {
            case EMAIL:
                return customer.getEmail();
            default:
                return customer.getContactNumber();
        }
    }

    /**
     * Calculates plus or minus days and apply the respective calculation to service due date.
     *
     * @param serviceDueDateRecommended
     * @param reminderPrefDTO
     * @return
     */
    private LocalDate reminderDateBasedOnPreference(LocalDate serviceDueDateRecommended,
                                                    ServiceReminderPrefDTO reminderPrefDTO) {
        int totalDays = 0;
        final var durationValue = reminderPrefDTO.getDurationValue();
        switch (reminderPrefDTO.getTimeFrame()) {
            case DAYS:
                totalDays = durationValue;
                break;
            case MONTHS:
                totalDays = (durationValue * 365) / 12;
                break;
            case WEEKS:
                totalDays = durationValue * 7;
                break;
            case YEARS:
                totalDays = durationValue * 365;
                break;

        }

        switch (reminderPrefDTO.getExpression()) {
            case BEFORE:
                return serviceDueDateRecommended.minusDays(totalDays);
            case AFTER:
                return serviceDueDateRecommended.plusDays(totalDays);
        }

        return null;
    }

    private List<ServiceReminderPrefDTO> fetchReminderPreferencesByServiceType(int serviceType) {
        return mdServiceReminderPrefService.fetchActiveReminderPreferencesByServiceType(serviceType);
    }

    private ServiceLogicReminderDetails fetchReminderDetailsByServiceType(int serviceType) {
        return serviceLogicReminderService.fetchServiceLogicReminderDetailsBasedOnServiceTypeId(serviceType);
    }
}
