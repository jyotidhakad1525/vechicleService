package com.automate.vehicleservices.api;

import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import com.automate.vehicleservices.entity.enums.ReminderStatus;
import com.automate.vehicleservices.framework.api.APIResponse;
import com.automate.vehicleservices.framework.api.AbstractBaseController;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.service.ServiceReminderService;
import com.automate.vehicleservices.service.dto.ServiceReminderDTO;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * @author Chandrashekar V
 */
@Tag(name = "Service Reminder", description = "Service Reminder API")
@RestController
@RequestMapping(value = AbstractBaseController.BASE_URL + "/service-reminder")
@Slf4j
public class VehicleServiceReminderController extends AbstractBaseController {

    private final ServiceReminderService serviceReminderService;

    public VehicleServiceReminderController(ServiceReminderService serviceReminderService) {
        this.serviceReminderService = serviceReminderService;
    }

    @Operation(summary = "API to fetch service Reminder of all vehicles by tenant",
            description = "Service Reminders of all the vehicles of the given Tenant.",
            tags = {"Service Reminder"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @GetMapping
    public ResponseEntity<APIResponse<?>> serviceReminderByTenant(@PathVariable(TENANT) final String tenant,
                                                                  @RequestParam(required = false) ReminderStatus[] ReminderStatuses) {

        List<ServiceReminderDTO> reminders = null;
        if (Objects.isNull(ReminderStatuses) || ReminderStatuses.length == 0) {
            reminders =
                    serviceReminderService.findRemindersByTenantAndStatus(getTenant()
                            , ReminderStatus.values());
        } else
            // if no status provided explicitly, then return active reminders only
            reminders = serviceReminderService.findActiveRemindersByTenant(tenant);

        Map<String, ReminderByTenant> reminderByTenantMap = trimmedVersionOfReminderData(reminders);
        return responseEntity(reminderByTenantMap);
    }

    public Map<String, ReminderByTenant> trimmedVersionOfReminderData(List<ServiceReminderDTO> reminders) {
        Map<String, ReminderByTenant> reminderByTenantMap = new HashMap<>();

        for (ServiceReminderDTO reminderDTO : reminders) {
            reminderByTenantMap.computeIfAbsent(reminderDTO.getTenant().getIdentifier(),
                    v -> new ReminderByTenant(reminderDTO.getTenant().getIdentifier()))
                    .getRemindersByVehicle()
                    .computeIfAbsent(reminderDTO.getServiceVehicle().getRegNumber(),
                            v -> new ReminderByVehicle(new TrimmedVehicle(reminderDTO)))
                    .getServiceReminderMap()
                    .computeIfAbsent(reminderDTO.getId(),
                            v ->
                                    getTrimmedServiceReminder(reminderDTO))
                    .add(reminderDTO.getServiceReminderDetails());


        }
        return reminderByTenantMap;
    }

    public TrimmedServiceReminder getTrimmedServiceReminder(ServiceReminderDTO reminderDTO) {
        return new TrimmedServiceReminder(reminderDTO.getId(), reminderDTO.getReminderStartDate(),
                reminderDTO.getRemindUntil(), new TrimmedServiceSchedule(reminderDTO),
                new TrimmedServiceCriteria(reminderDTO));
    }

    @Operation(summary = "API to fetch service Reminder of a given vehicle",
            description = "Service Reminder of a given vehicle.",
            tags = {"Service Reminder"}, parameters = {@Parameter(in = ParameterIn.PATH, required = true, name =
            TENANT)})
    @GetMapping("/{vehicle-reg-number}")
    public ResponseEntity<APIResponse<?>> serviceReminderByVehicle(@PathVariable(TENANT) final String tenant,
                                                                   @PathVariable("vehicle-reg-number") String vehicleRegNumber,
                                                                   @RequestParam(required = false) ReminderStatus[] reminderStatuses) {
        List<ServiceReminderDTO> reminders = null;

        // Unless explicitly specified fetch only active reminders for the given vehicle
        if (Objects.isNull(reminderStatuses) || reminderStatuses.length == 0) {
            reminders =
                    serviceReminderService.findActiveRemindersForVehicle(vehicleRegNumber);
        } else
            reminders = serviceReminderService.findRemindersByVehicle(vehicleRegNumber, reminderStatuses);

        Map<String, ReminderByTenant> reminderByTenantMap = trimmedVersionOfReminderData(reminders);
        return responseEntity(reminderByTenantMap);

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class ReminderByTenant {
        private String tenant;
        private Map<String, ReminderByVehicle> remindersByVehicle = new HashMap<>();

        public ReminderByTenant(String tenant) {
            this.tenant = tenant;
        }

        @JsonIgnore
        public Map<String, ReminderByVehicle> getRemindersByVehicle() {
            return remindersByVehicle;
        }

        public Collection<ReminderByVehicle> getReminders() {
            return remindersByVehicle.values();
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class ReminderByVehicle {
        private TrimmedVehicle vehicle;
        private Map<Integer, TrimmedServiceReminder> serviceReminderMap = new HashMap<>();


        public ReminderByVehicle(TrimmedVehicle vehicle) {
            this.vehicle = vehicle;
        }

        @JsonIgnore
        public Map<Integer, TrimmedServiceReminder> getServiceReminderMap() {
            return serviceReminderMap;
        }

        public Collection<TrimmedServiceReminder> getReminders() {
            return getServiceReminderMap().values();
        }

        protected ReminderByVehicle addReminder(TrimmedServiceReminder reminder) {
            serviceReminderMap.putIfAbsent(reminder.getId(), reminder);
            return this;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class TrimmedServiceCriteria {
        private String serviceName;
        private int durationDaysTo;
        private int durationDaysFrom;
        private int mileageFrom;
        private int mileageTo;

        public TrimmedServiceCriteria(ServiceReminderDTO reminderDTO) {
            this.serviceName = reminderDTO.getServiceType().getServiceName();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class TrimmedServiceSchedule {
        private LocalDate serviceDateAsPerSchedule;
        private LocalDate serviceDatePreferred;
        private LocalDate serviceDateRecommended;
        private LocalDateTime scheduledDate;

        public TrimmedServiceSchedule(ServiceReminderDTO reminderDTO) {
            final var vehicleServiceSchedule = reminderDTO.getVehicleServiceSchedule();
            this.serviceDateAsPerSchedule = vehicleServiceSchedule.getServiceDueDatePerSchedule();
            this.serviceDatePreferred = vehicleServiceSchedule.getServiceDueDatePreferred();
            this.serviceDateRecommended = vehicleServiceSchedule.getServiceDueDateRecommended();
            this.scheduledDate = vehicleServiceSchedule.getCreatedDate();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    static class TrimmedServiceReminderDetails {
        private CommunicationModeEnum mode;
        private String message;
        private String address;
        private LocalDate reminderDate;

        public TrimmedServiceReminderDetails(ServiceReminderDetailsDTO reminderDetailsDTO) {
            if (null == reminderDetailsDTO)
                return;
            this.mode = reminderDetailsDTO.getCommunicationMode().getType();
            this.message = reminderDetailsDTO.getMessageBody();
            this.address = reminderDetailsDTO.getCommunicationAddress();
            this.reminderDate = reminderDetailsDTO.getDateOfReminder();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class TrimmedVehicle {
        private String vehicle;
        private int kmReading;
        private int age;
        private LocalDate purchaseDate;

        public TrimmedVehicle(ServiceReminderDTO serviceReminderDTO) {
            ServiceScheduleDTO serviceScheduleDTO = serviceReminderDTO.getVehicleServiceSchedule();
            final var serviceVehicle = serviceScheduleDTO.getServiceVehicle();
            this.vehicle = serviceVehicle.getRegNumber();
            this.age = (int) Math.abs(ChronoUnit.DAYS.between(serviceVehicle.getPurchaseDate(),
                    serviceScheduleDTO.getCreatedDate().toLocalDate()));
            this.purchaseDate = serviceVehicle.getPurchaseDate();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonRootName("serviceReminder")
    static class TrimmedServiceReminder {
        private int id;
        private LocalDate startDate;
        private LocalDate remindUntil;
        private TrimmedServiceSchedule serviceSchedule;
        private Map<LocalDate, Set<TrimmedServiceReminderDetails>> reminders = new HashMap<>();
        private TrimmedServiceCriteria service;

        public TrimmedServiceReminder(int id, LocalDate startDate, LocalDate remindUntil,
                                      TrimmedServiceSchedule serviceSchedule, TrimmedServiceCriteria service) {
            this.id = id;
            this.startDate = startDate;
            this.remindUntil = remindUntil;
            this.serviceSchedule = serviceSchedule;
            this.service = service;
        }

        public void add(List<ServiceReminderDetailsDTO> trimmedServiceReminderDetails) {
            if (trimmedServiceReminderDetails == null)
                return;

            trimmedServiceReminderDetails.stream().map(TrimmedServiceReminderDetails::new)
                    .forEach(detail -> reminders.computeIfAbsent(detail.getReminderDate()
                            , v -> new HashSet<>()).add(detail));
        }

    }
}
