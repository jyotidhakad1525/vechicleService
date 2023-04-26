package com.automate.vehicleservices.schedule;

import com.automate.vehicleservices.api.filter.RequestContext;
import com.automate.vehicleservices.entity.MdServiceType;
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
import com.automate.vehicleservices.service.*;
import com.automate.vehicleservices.service.dto.MdServiceSchedulingConfigDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * vehicle service schedule happens here.
 */
@Component
@Slf4j
public class SchedulingCoreEngine {
    private final ServiceVehicleService serviceVehicleService;
    private final MdServiceTypeService serviceTypeService;
    private final DateHelper dateHelper;
    private final RequestContext requestContext;
    private final VehicleServiceHistoryService serviceHistoryService;
    private final MdTenantService mdTenantService;
    private final CrudService crudService;
    private final SchedulingConfig schedulingConfig;
    private final Map<String, List<String>> events = new ConcurrentHashMap<>();

    public SchedulingCoreEngine(ServiceVehicleService serviceVehicleService,
                                MdServiceTypeService serviceTypeService, DateHelper dateHelper,
                                RequestContext requestContext,
                                MdServiceCategoryService mdServiceCategoryService,
                                VehicleServiceHistoryService serviceHistoryService,
                                MdTenantService mdTenantService, CrudService crudService,
                                SchedulingConfig schedulingConfig) {
        this.serviceVehicleService = serviceVehicleService;
        this.serviceTypeService = serviceTypeService;
        this.dateHelper = dateHelper;
        this.requestContext = requestContext;
        this.serviceHistoryService = serviceHistoryService;
        this.mdTenantService = mdTenantService;
        this.crudService = crudService;
        this.schedulingConfig = schedulingConfig;
    }

    @Transactional
    public List<ServiceScheduleDTO> schedule(final ServiceVehicleDTO serviceVehicle) {

        final var config = schedulingConfig.config(serviceVehicle.getTenantId());
        if (Objects.isNull(config))
            throw new VehicleServicesException(String.format("Tenant configuration is not set for %s",
                    serviceVehicle.getTenantId()));
        try {
            addEvent(serviceVehicle.getRegNumber(), String.format(" Started scheduling engine for vehicle : %s",
                    serviceVehicle.toString()));

            if (Objects.isNull(serviceVehicle.getPurchaseDate())) {
                addEvent(serviceVehicle.getRegNumber(), "PurchaseDate is null. Exiting...");
                throw new VehicleServicesException(String.format("Missing Vehicle Purchase data..%s"));
            }
            List<VehicleServiceHistoryDTO> vehicleServiceHistory = vehicleServiceHistory(serviceVehicle);
            List<ServiceDataContainer> serviceDataContainers = null;
            if (!CollectionUtils.isEmpty(vehicleServiceHistory)) {
                // When service history is not empty, due date will be calculated based on service history.
                serviceDataContainers = byServiceHistory(serviceVehicle, vehicleServiceHistory, config);
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

        final var persist = persist(nextServiceSchedule);

        addEvent(serviceVehicle.getRegNumber(), nextServiceSchedule.toString());
        return transformIntoDTO(persist, serviceVehicle);
    }

    /**
     * Initiates calculation by service history.
     *
     * @param serviceVehicle
     * @param vehicleServiceHistory
     * @return
     */
    private List<ServiceDataContainer> byServiceHistory(ServiceVehicleDTO serviceVehicle,
                                                        List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                        MdServiceSchedulingConfigDTO config) {
        addEvent(serviceVehicle.getRegNumber(),
                String.format("Calculating Due date by vehicle service history:"));
        addEvent(serviceVehicle.getRegNumber(),
                vehicleServiceHistory.stream().map(s -> s.toString()).reduce((a, b) -> a + " \n\n " + b).get());
        var serviceDataContainer = dueDateByServiceHistory(serviceVehicle, vehicleServiceHistory, config);
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
                                                           MdServiceSchedulingConfigDTO config) {


        addEvent(serviceVehicle.getRegNumber(), String.format(" Service History doesn't exists."));

        // Check whether vehicle is due for FIRST SERVICE.
        int vehicleAge = calculateVehicleAge(serviceVehicle);

        addEvent(serviceVehicle.getRegNumber(), String.format("Vehicle Age %d", vehicleAge));
        log.info(serviceVehicle.getRegNumber(), String.format("Vehicle Age %d", vehicleAge));

        LocalDate serviceDueDate = null;
        if (vehicleAge <= config.getFirstFreeServiceDueDaysFromPurchaseDate()) {
            serviceDueDate = dateHelper
                    .addDays(serviceVehicle.getPurchaseDate(), config.getFirstFreeServiceDueDaysFromPurchaseDate());
        } else {
            addEvent(serviceVehicle.getRegNumber(),
                    String.format("returning null. Vehicle age is beyond first service " +
                            "due days."));

            return null;
        }

        Optional<MdServiceType> byServiceName = crudService
                .findById(config.getFirstFreeServiceType(), MdServiceType.class);

        var serviceDataContainer =
                ServiceDataContainer.builder().nextServiceRecommendedDate(serviceDueDate)
                        .nextServiceDateAsPerSchedule(serviceDueDate)
                        .nextServiceType(byServiceName.isPresent() ? byServiceName.get().getId() : 0)
                        .serviceCategory(config.getFreeServiceCategoryName())
                        .differenceInDaysBetweenLastTwoServices(0)
                        .kmBetweenLastTwoServices(0)
                        .averageKM(0)
                        .lastServiceDate(null)
                        .build();

        addEvent(serviceVehicle.getRegNumber(), String.format("Calculated Due date by vehicle Age: %s",
                serviceDataContainer.toString()));

        // PMC date is not relevant to vehicle that is recently purchased. Hence 'null' in place of PMC data.
        return List.of(serviceDataContainer);
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

    /**
     * Calculates due date by service history data. Invokes respective methods for one service history entry exists
     * or multiple exist.
     *
     * @param serviceVehicle
     * @param vehicleServiceHistory
     * @return
     */
    private List<ServiceDataContainer> dueDateByServiceHistory(ServiceVehicleDTO serviceVehicle,
                                                               List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                               MdServiceSchedulingConfigDTO config) {
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
                                                                MdServiceSchedulingConfigDTO config) {

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
                                                         MdServiceSchedulingConfigDTO config) {

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
            LocalDate secondRecentServiceDate, MdServiceSchedulingConfigDTO config) {

        long differenceInDays = dateHelper
                .differenceInDays(recentServiceHistoryDTO.getServiceDate(), secondRecentServiceDate);
        int noKMRanBetweenTwoServices = recentServiceHistoryDTO.getKmReading() - secondRecentServiceKmReading;
        if (differenceInDays < 0 || noKMRanBetweenTwoServices <= 0) {
            log.warn(
                    String.format("Invalid/Corrupted data. Difference in days between last two services %d, no.of km " +
                            "between last two services %d", differenceInDays, noKMRanBetweenTwoServices));
            return null;
        }

        if (differenceInDays == 0)
            differenceInDays = 1;

        float averageKM = (float) noKMRanBetweenTwoServices / differenceInDays;

        ServiceDataContainer pmsDataContainer = pmsDate(vehicleServiceHistory, serviceVehicleDTO,
                recentServiceHistoryDTO, config, differenceInDays,
                noKMRanBetweenTwoServices, averageKM);

        // PMC date - For PMC Next service is always PMC.
        Optional<MdServiceType> pmcServiceType = crudService
                .findById(config.getPmcServiceType(), MdServiceType.class);
        final var serviceTypeId = pmcServiceType.isPresent() ? pmcServiceType.get().getId() : 0;
        var pmcDataContainer = nextServiceDueDate(serviceVehicleDTO, averageKM,
                recentServiceHistoryDTO.getServiceDate(),
                config.getPmcIntervalMonths(),
                config.getPmcIntervalKm(),
                serviceTypeId, noKMRanBetweenTwoServices,
                differenceInDays, config.getPmcServiceCategoryName());

        return List.of(pmsDataContainer, pmcDataContainer);
    }

    /**
     * Calculates next Pms Due date.
     *
     * @param vehicleServiceHistory
     * @param serviceVehicleDTO
     * @param recentServiceHistoryDTO
     * @param config
     * @param differenceInDays
     * @param noKMRanBetweenTwoServices
     * @param averageKM
     * @return
     */
    private ServiceDataContainer pmsDate(List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                         ServiceVehicleDTO serviceVehicleDTO,
                                         VehicleServiceHistoryDTO recentServiceHistoryDTO,
                                         MdServiceSchedulingConfigDTO config, long differenceInDays,
                                         int noKMRanBetweenTwoServices,
                                         float averageKM) {
        RecentPMSDetails recentPMSDetails = new RecentPMSDetails(vehicleServiceHistory,
                serviceVehicleDTO.getRegNumber(),
                recentServiceHistoryDTO, differenceInDays, noKMRanBetweenTwoServices).invoke(config);

        LocalDate recentPmsServiceDate = recentPMSDetails.getRecentPmsServiceDate();
        int recentPMSServiceType = recentPMSDetails.getRecentPMSServiceType();

        // PMS date
        MdServiceType nextServiceInSequence = serviceTypeService
                .findNextServiceInSequence(recentPMSServiceType, serviceVehicleDTO.getTenantId());
        final var nextServiceTypeId = null != nextServiceInSequence ?
                nextServiceInSequence.getId() : 0;

        return nextServiceDueDate(serviceVehicleDTO, averageKM, recentPmsServiceDate,
                config.getPmsIntervalMonths(), config.getPmsIntervalKm(), nextServiceTypeId,
                noKMRanBetweenTwoServices, differenceInDays, config.getPmsServiceCategoryName());


    }

    /**
     * Next service due date calculation.
     *
     * @param serviceVehicleDTO
     * @param averageKM
     * @param recentServiceDate
     * @param intervalMonths
     * @param intervalKM
     * @param nextServiceId
     * @param noKMRanBetweenTwoServices
     * @param differenceInDays
     * @param serviceCategoryName
     * @return
     */
    private ServiceDataContainer nextServiceDueDate(ServiceVehicleDTO serviceVehicleDTO, float averageKM,
                                                    LocalDate recentServiceDate, int intervalMonths, int intervalKM,
                                                    int nextServiceId, int noKMRanBetweenTwoServices,
                                                    long differenceInDays, String serviceCategoryName) {
        if (Objects.isNull(recentServiceDate))
            return null;

        final String vehicleRegNumber = serviceVehicleDTO.getRegNumber();

        LocalDate byScheduledInterval = dateHelper.addMonths(recentServiceDate, intervalMonths);

        addEvent(vehicleRegNumber, String.format("Adding %d months to %s to calculate next Service " +
                "Date: %s", intervalMonths, recentServiceDate, byScheduledInterval));

        addEvent(vehicleRegNumber, String.format("Average KM : %s", averageKM));

        LocalDate byAverageKM = dateHelper
                .addDays(recentServiceDate, Math.round(intervalKM / averageKM));

        addEvent(vehicleRegNumber, String.format("Adding %d days to %s to calculate next Service " +
                "Date: %s", Math.round(intervalKM / averageKM), recentServiceDate, byAverageKM));

        log.info(String.format("Earlier date between  Scheduled interval %s, by Average KM %s ",
                byScheduledInterval, byAverageKM));
        LocalDate recommendedServiceDate = earlierDate(byScheduledInterval, byAverageKM);

        // If Past Date Then Return Current Date
        recommendedServiceDate =
                (Objects.nonNull(recommendedServiceDate) && recommendedServiceDate.isBefore(LocalDate.now())) ?
                        LocalDate.now() : recommendedServiceDate;
        addEvent(vehicleRegNumber, String.format("Next Service Date: %s", recommendedServiceDate));

        return ServiceDataContainer.builder()
                .nextServiceDateAsPerKMAverage(byAverageKM)
                .nextServiceDateAsPerSchedule(byScheduledInterval)
                .nextServiceRecommendedDate(recommendedServiceDate)
                .nextServiceType(nextServiceId)
                .averageKM(averageKM)
                .kmBetweenLastTwoServices(noKMRanBetweenTwoServices)
                .differenceInDaysBetweenLastTwoServices((int) differenceInDays)
                .serviceCategory(serviceCategoryName)
                .lastServiceDate(recentServiceDate)
                .build();
    }

    private Optional<VehicleServiceHistoryDTO> recentPMS(List<VehicleServiceHistoryDTO> vehicleServiceHistory,
                                                         MdServiceSchedulingConfigDTO config) {

        return vehicleServiceHistory.stream().filter(history -> StringUtils
                .equalsIgnoreCase(config.getPmsServiceCategoryName(), history.getCategoryName())).findFirst();
    }

    private ServiceTypeDTO fetchServiceType(int serviceTypeId) {
        return serviceTypeService.findById(serviceTypeId);
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

    private boolean isVehicleActive(ServiceVehicleDTO vehicle) {
        return vehicle.getStatus() == VehicleStatus.ACTIVE;
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

    private class RecentPMSDetails {
        private final List<VehicleServiceHistoryDTO> vehicleServiceHistory;
        private final String vehicleRegNumber;
        private final VehicleServiceHistoryDTO recentServiceDTO;
        private final long differenceInDays;
        private final int noKMRanBetweenTwoServices;
        private LocalDate recentPmsServiceDate;
        private int recentPMSServiceType;

        public RecentPMSDetails(List<VehicleServiceHistoryDTO> vehicleServiceHistory, String vehicleRegNumber,
                                VehicleServiceHistoryDTO recentServiceDTO,
                                long differenceInDays,
                                int noKMRanBetweenTwoServices) {
            this.vehicleServiceHistory = vehicleServiceHistory;
            this.vehicleRegNumber = vehicleRegNumber;
            this.recentServiceDTO = recentServiceDTO;
            this.differenceInDays = differenceInDays;
            this.noKMRanBetweenTwoServices = noKMRanBetweenTwoServices;
        }

        public LocalDate getRecentPmsServiceDate() {
            return recentPmsServiceDate;
        }

        public int getRecentPMSServiceType() {
            return recentPMSServiceType;
        }

        public RecentPMSDetails invoke(MdServiceSchedulingConfigDTO config) {
            addEvent(vehicleRegNumber, String.format("Calculating Average KM -> No of KM between last two services %d" +
                    " in days %d", noKMRanBetweenTwoServices, differenceInDays));

            recentPmsServiceDate = null;
            recentPMSServiceType = 0;
            if (StringUtils.equalsIgnoreCase(recentServiceDTO.getCategoryName(),
                    config.getPmcServiceCategoryName()) || StringUtils
                    .equalsIgnoreCase(recentServiceDTO.getCategoryName(), config.getFreeServiceCategoryName())) {
                recentPmsServiceDate = recentServiceDTO.getServiceDate();
                recentPMSServiceType = recentServiceDTO.getServiceTypeId();
            } else {
                Optional<VehicleServiceHistoryDTO> recentPMS = recentPMS(vehicleServiceHistory, config);
                if (recentPMS.isPresent()) {
                    recentPmsServiceDate = recentPMS.get().getServiceDate();
                    recentPMSServiceType = recentPMS.get().getServiceTypeId();
                }
            }

            return this;
        }
    }
}


