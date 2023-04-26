package com.automate.vehicleservices.schedule.v1;

import com.automate.vehicleservices.api.filter.RequestContext;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.ServiceLogicConfigurationDetails;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleServiceSchedule;
import com.automate.vehicleservices.entity.builder.VehicleServiceScheduleBuilder;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.entity.enums.VehicleStatus;
import com.automate.vehicleservices.framework.common.DateHelper;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceTypeDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.repository.dtoprojection.VehicleServiceHistoryDTO;
import com.automate.vehicleservices.service.MdServiceCategoryService;
import com.automate.vehicleservices.service.MdServiceTypeService;
import com.automate.vehicleservices.service.MdTenantService;
import com.automate.vehicleservices.service.ServiceVehicleService;
import com.automate.vehicleservices.service.VehicleServiceHistoryService;
import com.automate.vehicleservices.service.dto.MdServiceSchedulingConfigDTO;
import com.automate.vehicleservices.service.dto.ServiceLogicConfDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * vehicle service schedule happens here.
 */
@Component("SchedulingCoreEngineV1")
@Slf4j
public class SchedulingCoreEngine {
    private final ServiceVehicleService serviceVehicleService;
    private final MdServiceTypeService serviceTypeService;
    private final DateHelper dateHelper;
    private final VehicleServiceHistoryService serviceHistoryService;
    private final MdTenantService mdTenantService;
    private final CrudService crudService;
    private final SchedulingConfig schedulingConfig;
    private final Map<String, List<String>> events = new ConcurrentHashMap<>();

    public SchedulingCoreEngine(ServiceVehicleService serviceVehicleService,
                                MdServiceTypeService serviceTypeService, DateHelper dateHelper,
                                MdServiceCategoryService mdServiceCategoryService,
                                VehicleServiceHistoryService serviceHistoryService,
                                MdTenantService mdTenantService, CrudService crudService,
                                @Qualifier("SchedulingConfigV1") SchedulingConfig schedulingConfig) {
        this.serviceVehicleService = serviceVehicleService;
        this.serviceTypeService = serviceTypeService;
        this.dateHelper = dateHelper;
        this.serviceHistoryService = serviceHistoryService;
        this.mdTenantService = mdTenantService;
        this.crudService = crudService;
        this.schedulingConfig = schedulingConfig;
    }

    @Transactional
    public List<ServiceScheduleDTO> schedule(final ServiceVehicleDTO serviceVehicle) {

        final var config = schedulingConfig.config(serviceVehicle.getTenantId());
        if (config.isEmpty())
            throw new VehicleServicesException(String.format("Tenant configuration is not set for %s",
                    serviceVehicle.getTenantId()));
        try {
            addEvent(serviceVehicle.getRegNumber(), String.format(" Started scheduling engine for vehicle : %s",
                    serviceVehicle.toString()));

            if (Objects.isNull(serviceVehicle.getPurchaseDate())) {
                addEvent(serviceVehicle.getRegNumber(), "PurchaseDate is null. Exiting...");
                throw new VehicleServicesException(String.format("Missing Vehicle Purchase data..%s"));
            }
//            List<VehicleServiceHistoryDTO> vehicleServiceHistory = vehicleServiceHistory(serviceVehicle);
            List<VehicleServiceHistoryDTO> vehicleServiceHistory = crudService.findById(serviceVehicle.getId(), ServiceVehicle.class)
                    .get()
                    .getVehicleServiceHistories()
                    .stream()
                    .filter(history1 -> Objects.nonNull(history1.getMdServiceType()) && Objects.nonNull(history1.getKmReading()) && Objects.nonNull(history1.getServiceDate()))
                    .filter(history -> history.getMdServiceType().getMdServiceCategory().getServiceGroup().equals(ServiceGroup.REGULAR_MAINTENANCE))
                    .map(VehicleServiceHistoryDTO::new).collect(Collectors.toList());

            List<ServiceDataContainer> serviceDataContainers = null;
            if (!CollectionUtils.isEmpty(vehicleServiceHistory)) {
                // When service history is not empty, due date will be calculated based on service history.
                serviceDataContainers = byServiceHistoryV1(serviceVehicle, vehicleServiceHistory, config);
            } else {
                serviceDataContainers = dueDateByVehicleAge(serviceVehicle, config);
            }

            return persistSchedules(serviceVehicle, vehicleServiceHistory, serviceDataContainers);

        } catch (Exception e) {
            e.printStackTrace(); // TEMP to capture root cause until flow is stable.
            throw new VehicleServicesException(String.format("Scheduling failed..%s", e.getMessage()));
        }
        /*finally {//temp to write to console
            try {
                FileUtils.writeLines(new File("output" + serviceVehicle.getRegNumber() + ".txt"),
                        events.get(serviceVehicle.getRegNumber()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * persist schedules to database.
     *
     * @param serviceVehicle
     * @param vehicleServiceHistory
     * @param serviceDataContainers
     * @return
     */
    private List<ServiceScheduleDTO> persistSchedules(ServiceVehicleDTO serviceVehicle,
                                                      List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                      List<ServiceDataContainer> serviceDataContainers) {
        VehicleServiceScheduleBuilder vehicleServiceScheduleBuilder =
                VehicleServiceScheduleBuilder.aVehicleServiceSchedule();

        // Extract recently done service date from service history, if exists.
        if (CollectionUtils.isNotEmpty(vehicleServiceHistory)) {
            vehicleServiceScheduleBuilder.withLastServiceDate(vehicleServiceHistory.get(0).getServiceDate());
        }

        if (Objects.isNull(serviceDataContainers))
            return null;

        return serviceDataContainers.stream()
                .filter(Objects::nonNull)
                .filter(this::isNextScheduleAvailable)
                .map(serviceDataContainer -> constructServiceScheduleFromServiceDataContainer(serviceVehicle,
                        vehicleServiceScheduleBuilder, serviceDataContainer))
                .collect(Collectors.toList());

    }

    /**
     * Constructs the final service schedule from the calculated service data.
     *
     * @param serviceVehicle
     * @param vehicleServiceScheduleBuilder
     * @param serviceDataContainer
     * @return
     */
    private ServiceScheduleDTO constructServiceScheduleFromServiceDataContainer(ServiceVehicleDTO serviceVehicle,
                                                                                VehicleServiceScheduleBuilder vehicleServiceScheduleBuilder,
                                                                                ServiceDataContainer serviceDataContainer) {
        MdServiceType mdServiceType = null;
        if (serviceDataContainer.getNextServiceType() != 0) {
            mdServiceType = crudService.findById(serviceDataContainer.getNextServiceType(), MdServiceType.class)
                    .orElse(null);
        }

        final var averageKM = serviceDataContainer.getAverageKM();
        final var nextServiceSchedule = vehicleServiceScheduleBuilder
                .withServiceDueDatePerSchedule(serviceDataContainer.getNextServiceDateAsPerSchedule())
                .withServiceVehicle(serviceVehicleService.findByVehicleRegNumber(serviceVehicle.getRegNumber()))
                .withMdServiceType(mdServiceType)
                .withServiceDueDatePreferred(serviceDataContainer.getNextServiceDateAsPerKMAverage())
                .withServiceDueDateRecommended(serviceDataContainer.getNextServiceRecommendedDate())
                .withMdTenant(mdTenantService.tenantByIdentifier(serviceVehicle.getTenantId()))
                .withStatus(ScheduleStatus.NEW)
                .withDifferenceInDaysBetweenLastTwoServices(
                        serviceDataContainer.getDifferenceInDaysBetweenLastTwoServices())
                .withKmBetweenLastTwoServices(serviceDataContainer.getKmBetweenLastTwoServices())
                .withAverageKM(averageKM)
                .withLastServiceDate(serviceDataContainer.getLastServiceDate())
                .withLastModifiedReason("Created new Schedule")
                .withServiceCategory(serviceDataContainer.getServiceCategory())
                .build();

        serviceVehicleService.updateOnGoingDueDateAndServiceTypeBasedOnVehicleId(serviceVehicle.getId(),mdServiceType,serviceDataContainer.getNextServiceRecommendedDate());

        final var persist = persist(nextServiceSchedule);

        addEvent(serviceVehicle.getRegNumber(), nextServiceSchedule.toString());
        return transformIntoDTO(persist, serviceVehicle);
    }


    private List<ServiceDataContainer> byServiceHistoryV1(ServiceVehicleDTO serviceVehicle,
                                                          List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                          List<ServiceLogicConfigurationDetails> config) {
//        var serviceDataContainer;
        addEvent(serviceVehicle.getRegNumber(),
                String.format("Calculating Due date by vehicle service history:"));
        addEvent(serviceVehicle.getRegNumber(),
                vehicleServiceHistory.stream().map(s -> s.toString()).reduce((a, b) -> a + " \n\n " + b).get());
        var serviceDataContainer = dueDateByServiceHistoryV1(serviceVehicle, vehicleServiceHistory, config);
        addEvent(serviceVehicle.getRegNumber(), String.format("Calculated Due date by service history: %s",
                null != serviceDataContainer ? serviceDataContainer.toString() : StringUtils.EMPTY));

        return serviceDataContainer;
    }

    /**
     * Calculates next service due date by vehicle purchase date.
     *
     * @param serviceVehicle
     * @param config
     * @return
     */
    private List<ServiceDataContainer> dueDateByVehicleAge(ServiceVehicleDTO serviceVehicle,
                                                           List<ServiceLogicConfigurationDetails> config) {


        addEvent(serviceVehicle.getRegNumber(), String.format(" Service History doesn't exists."));

        // Check whether vehicle is due for FIRST SERVICE.
        Integer vehicleAge = calculateVehicleAge(serviceVehicle);   // 32   // 2nd                      //32 2nd
        Integer vehicleCurrentKm = serviceVehicle.getCurrentKmReading();  // 20000 // 3rd               // 500km 1st

        addEvent(serviceVehicle.getRegNumber(), String.format("Vehicle Age %d", vehicleAge));
        log.info(serviceVehicle.getRegNumber(), String.format("Vehicle Age %d", vehicleAge));

        Optional<ServiceLogicConfigurationDetails> kmBased = config.stream().filter(data -> vehicleCurrentKm > data.getKmStart() && vehicleCurrentKm < data.getKmEnd()).findFirst();

        Optional<ServiceLogicConfigurationDetails> daysBased = config.stream().filter(data -> vehicleAge > data.getStartDay() && vehicleAge < data.getEndDay()).findFirst();

        if (!kmBased.isPresent() && !daysBased.isPresent()) {
            log.error("data is not present !!!");
            throw new VehicleServicesException("Service Data is not present proper!!");
        }

        int kmSequence = kmBased.get().getSubServiceType().getServiceSequence(); // 3       //1
        int daysSequence = daysBased.get().getSubServiceType().getServiceSequence(); // 2   //2

        MdServiceType serviceType;
        ServiceLogicConfigurationDetails details;
        if (kmSequence > daysSequence) {  // 3rd            // 2nd
            serviceType = kmBased.get().getSubServiceType();
            details = kmBased.get();
        } else {
            serviceType = daysBased.get().getSubServiceType();
            details = daysBased.get();
        }
        LocalDate dueDate = getDueDate(vehicleCurrentKm, vehicleAge, details, serviceVehicle);

        var serviceDataContainer =
                ServiceDataContainer.builder().nextServiceRecommendedDate(dueDate)
                        .nextServiceDateAsPerSchedule(dueDate)
                        .nextServiceType(serviceType.getId())
                        .serviceCategory(serviceType.getMdServiceCategory().getCategoryName())
                        .differenceInDaysBetweenLastTwoServices(0)
                        .kmBetweenLastTwoServices(0)
                        .averageKM(0)
                        .lastServiceDate(null)
                        .build();


        addEvent(serviceVehicle.getRegNumber(), String.format("Calculated Due date by vehicle Age: %s",
                serviceDataContainer.toString()));

        return List.of(serviceDataContainer);
    }

    private LocalDate getDueDate(int vehicleCurrentKm, int vehicleAge, ServiceLogicConfigurationDetails details, ServiceVehicleDTO serviceVehicle) {
        LocalDate dueDate;
        //per day
        int avgKM = Math.round(vehicleCurrentKm / vehicleAge);   // 625km    // 50km

        int basedKmDueDays = Math.round(details.getKmEnd() / avgKM); // 39  // 400

        if (basedKmDueDays < details.getEndDay()) {
            dueDate = dateHelper
                    .addDays(serviceVehicle.getPurchaseDate(), basedKmDueDays);
        } else {
            dueDate = dateHelper
                    .addDays(serviceVehicle.getPurchaseDate(), details.getEndDay());
        }
        return dueDate;
    }

    private boolean isNextScheduleAvailable(ServiceDataContainer serviceDataContainer) {
        return null != serviceDataContainer && serviceDataContainer.getNextServiceRecommendedDate() != null;
    }

    private ServiceScheduleDTO transformIntoDTO(VehicleServiceSchedule newSchedule,
                                                ServiceVehicleDTO serviceVehicleDTO) {

        final var mdServiceType = newSchedule.getMdServiceType();
        final var serviceScheduleDTOBuilder = ServiceScheduleDTO.builder()
                .id(newSchedule.getId())
                .status(newSchedule.getStatus())
                .tenantIdentifier(newSchedule.getMdTenant().getTenantIdentifier())
                .serviceVehicle(serviceVehicleDTO)
                .serviceDueDatePerSchedule(newSchedule.getServiceDueDatePerSchedule())
                .serviceDueDatePreferred(newSchedule.getServiceDueDatePreferred())
                .serviceDueDateRecommended(newSchedule.getServiceDueDateRecommended());
        if (mdServiceType != null)
            serviceScheduleDTOBuilder.serviceType(mdServiceType.getId());
        return serviceScheduleDTOBuilder.build();
    }

    private VehicleServiceSchedule persist(VehicleServiceSchedule vehicleServiceSchedule) {
        log.info(String.format("Schedule: %s", vehicleServiceSchedule.toString()));
        return crudService.save(vehicleServiceSchedule);
    }

    private void addEvent(final String key, final String event) {
        events.computeIfAbsent(key, k -> new ArrayList<>()).add(event.concat("\n"));
    }

    private List<ServiceDataContainer> dueDateByServiceHistoryV1(ServiceVehicleDTO serviceVehicle,
                                                               List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                                 List<ServiceLogicConfigurationDetails> config) {
        addEvent(serviceVehicle.getRegNumber(),
                String.format("ServiceHistory size: %s", vehicleServiceHistory.size()));

        if (vehicleServiceHistory.size() == 1) {
            // Use purchase date to determine average KM
            return usingSalesAndServiceDate(vehicleServiceHistory, serviceVehicle, config);
        }
        // calculate Average between last two services. Service history is in sorted order by service date
        return usingServiceDates(vehicleServiceHistory, serviceVehicle, config);
    }

    /**
     * This should be applicable only when there is only entry in service history.
     *
     * @param vehicleServiceHistory
     * @param serviceVehicleDTO
     * @return
     */
    private List<ServiceDataContainer> usingSalesAndServiceDate(List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                                ServiceVehicleDTO serviceVehicleDTO,
                                                                List<ServiceLogicConfigurationDetails> config) {

        VehicleServiceHistoryDTO recentServiceDTO = vehicleServiceHistory.get(0);

        /* Second recent service date in this case would be purchase
        date as there is only one service history exists. So we use purchase date as starting point to calculate
        Avg KM. Similarly, second recent service date would be vehicle purchase date. */

        return calculateNextServiceDueDates(vehicleServiceHistory, serviceVehicleDTO, recentServiceDTO,
                0, serviceVehicleDTO.getPurchaseDate(), config);
    }

    /**
     * When multiple services done earlier.
     *
     * @param vehicleServiceHistory
     * @param serviceVehicleDTO
     * @param config
     * @return
     */
    private List<ServiceDataContainer> usingServiceDates(List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                         final ServiceVehicleDTO serviceVehicleDTO,
                                                         List<ServiceLogicConfigurationDetails> config) {

        VehicleServiceHistoryDTO recentServiceHistoryDTO = vehicleServiceHistory.get(0);
        VehicleServiceHistoryDTO secondRecentServiceHistoryDTO = vehicleServiceHistory.get(1);
        final int secondRecentServiceKmReading = secondRecentServiceHistoryDTO.getKmReading();
        final LocalDate secondRecentServiceDate = secondRecentServiceHistoryDTO.getServiceDate();

        return calculateNextServiceDueDates(vehicleServiceHistory, serviceVehicleDTO, recentServiceHistoryDTO,
                secondRecentServiceKmReading, secondRecentServiceDate, config);
    }

    /**
     * Next service due date.
     *
     * @param vehicleServiceHistory
     * @param serviceVehicleDTO
     * @param recentServiceHistoryDTO
     * @param secondRecentServiceKmReading
     * @param secondRecentServiceDate
     * @param config
     * @return
     */
    private List<ServiceDataContainer> calculateNextServiceDueDates(
            List<VehicleServiceHistoryDTO> vehicleServiceHistory, ServiceVehicleDTO serviceVehicleDTO,
            VehicleServiceHistoryDTO recentServiceHistoryDTO, int secondRecentServiceKmReading,
            LocalDate secondRecentServiceDate, List<ServiceLogicConfigurationDetails> config) {

        long differenceInDays = dateHelper
                .differenceInDays(recentServiceHistoryDTO.getServiceDate(), secondRecentServiceDate);  // 300


        int noKMRanBetweenTwoServices = recentServiceHistoryDTO.getKmReading() - secondRecentServiceKmReading; //12500


        if (differenceInDays < 0 || noKMRanBetweenTwoServices <= 0) {
            log.warn(
                    String.format("Invalid/Corrupted data. Difference in days between last two services %d, no.of km " +
                            "between last two services %d", differenceInDays, noKMRanBetweenTwoServices));
            return null;
        }

        if (differenceInDays == 0)
            differenceInDays = 1;

        float averageKM = (float) noKMRanBetweenTwoServices / differenceInDays;  // 41


        LocalDate recentServiceDate = recentServiceHistoryDTO.getServiceDate();
        int recentPMSServiceType = recentServiceHistoryDTO.getServiceTypeId();

        // PMS date
        MdServiceType nextServiceInSequence = serviceTypeService
                .findNextServiceInSequence(recentPMSServiceType, serviceVehicleDTO.getTenantId());
        final var nextServiceTypeId = null != nextServiceInSequence ?
                nextServiceInSequence.getId() : 0;

       Optional<ServiceLogicConfigurationDetails> logicConfigurationDetailsOptional =  config.stream().filter(data->data.getSubServiceType().getId() == nextServiceInSequence.getId()).findFirst();

       if(!logicConfigurationDetailsOptional.isPresent()){
           log.error("config details not proper");
           throw new VehicleServicesException("Config details not proper!!");
       }

        ServiceLogicConfigurationDetails details = logicConfigurationDetailsOptional.get();

        if (Objects.isNull(recentServiceDate))
            return null;

        final String vehicleRegNumber = serviceVehicleDTO.getRegNumber();

        LocalDate byScheduledInterval = dateHelper.addDays(recentServiceDate, details.getEndDay());

        addEvent(vehicleRegNumber, String.format("Adding %d days to %s to calculate next Service " +
                "Date: %s", details.getEndDay(), recentServiceDate, byScheduledInterval));

        addEvent(vehicleRegNumber, String.format("Average KM : %s", averageKM));

        LocalDate byAverageKM = dateHelper
                .addDays(recentServiceDate, Math.round(details.getKmEnd() / averageKM));

        addEvent(vehicleRegNumber, String.format("Adding %d days to %s to calculate next Service " +
                "Date: %s", Math.round(details.getKmEnd() / averageKM), recentServiceDate, byAverageKM));

        log.info(String.format("Earlier date between  Scheduled interval %s, by Average KM %s ",
                byScheduledInterval, byAverageKM));
        LocalDate recommendedServiceDate = earlierDate(byScheduledInterval, byAverageKM);

        // If Past Date Then Return Current Date
        recommendedServiceDate =
                (Objects.nonNull(recommendedServiceDate) && recommendedServiceDate.isBefore(LocalDate.now())) ?
                        LocalDate.now() : recommendedServiceDate;
        addEvent(vehicleRegNumber, String.format("Next Service Date: %s", recommendedServiceDate));

        var dataContainer =   ServiceDataContainer.builder()
                .nextServiceDateAsPerKMAverage(byAverageKM)
                .nextServiceDateAsPerSchedule(byScheduledInterval)
                .nextServiceRecommendedDate(recommendedServiceDate)
                .nextServiceType(nextServiceTypeId)
                .averageKM(averageKM)
                .kmBetweenLastTwoServices(noKMRanBetweenTwoServices)
                .differenceInDaysBetweenLastTwoServices((int) differenceInDays)
                .serviceCategory(nextServiceInSequence.getMdServiceCategory().getCategoryName())
                .lastServiceDate(recentServiceDate)
                .build();


        return List.of(dataContainer);
    }

    private List<VehicleServiceHistoryDTO> vehicleServiceHistory(ServiceVehicleDTO serviceVehicle) {
        return serviceHistoryService
                .fetchServiceHistoryByVehicleCategory(serviceVehicle, ServiceGroup.REGULAR_MAINTENANCE);
    }

    private int calculateVehicleAge(ServiceVehicleDTO serviceVehicle) {
        return (int) dateHelper.differenceInDays(serviceVehicle.getPurchaseDate(), LocalDate.now());
    }

    private LocalDate earlierDate(LocalDate dueDateByLastService, LocalDate dueDateByVehicleAge) {
        return dueDateByLastService.isAfter(dueDateByVehicleAge) ? dueDateByVehicleAge : dueDateByLastService;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    private static class ScheduleLog {
        private final String message;
        private final LocalDate serviceDate;
        private final Map<String, Object> info = new HashMap<>();

        public ScheduleLog addInfo(final String key, final Object value) {
            this.info.computeIfPresent(key, (s, o) -> {
                if (!(o instanceof List)) {
                    List<Object> objects = new ArrayList<>();
                    objects.add(o);
                    objects.add(value);
                    return objects;
                } else {
                    ((List<Object>) o).add(value);
                    return o;
                }
            });
            return this;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    private static class ServiceDataContainer {
        private LocalDate nextServiceRecommendedDate;
        private LocalDate nextServiceDateAsPerSchedule;
        private LocalDate nextServiceDateAsPerKMAverage;
        private int previousPMSServiceTypeId;
        private int nextServiceType;
        private String serviceCategory;
        private int kmBetweenLastTwoServices;
        private int differenceInDaysBetweenLastTwoServices;
        private float averageKM;
        private LocalDate lastServiceDate;
    }

}


