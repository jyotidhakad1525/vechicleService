package com.automate.vehicleservices.schedule.v1;

import com.automate.vehicleservices.event.SchedulingSuccessEvent;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.service.ServiceReminderService;
import com.automate.vehicleservices.service.VehicleServiceScheduleService;
import com.automate.vehicleservices.service.dto.ServiceReminderDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Objects;

/**
 * Follows template method pattern to define the flow to schedule a vehicle. New, updated vehicle flows are handled by
 * concrete classes.
 */
@Slf4j
public abstract class AbstractVehicleSchedule implements VehicleSchedule {

    @Autowired
    protected VehicleServiceScheduleService vehicleServiceScheduleService;
    @Autowired
    @Qualifier("SchedulingCoreEngineV1")
    protected SchedulingCoreEngine vehicleCoreScheduleEngine;
    @Autowired
    protected EventPublisher eventPublisher;
    @Autowired
    protected ServiceReminderService serviceReminderService;

    /**
     * Scheduling gets initiated here.
     *
     * @param serviceVehicleDTO
     */
    public void schedule(ServiceVehicleDTO serviceVehicleDTO) {

        preRequisite(serviceVehicleDTO);

        // This is process where we have to save data in vehicle_service_scheduler
        List<ServiceScheduleDTO> serviceScheduleDTOs = proceed(serviceVehicleDTO);
        if (CollectionUtils.isEmpty(serviceScheduleDTOs))
            return;

        //
        serviceScheduleDTOs.forEach(this::postProcessing);
    }

    /**
     * <p>
     * 1. Take all unscheduled vehicles that do not have active reminders
     * <p>
     * 2. based on service type define reminder start date, and end date for each vehicle and persist the same in
     * service reminder table.
     * <p>
     * 3. While processing #2, also create reminder details entries for each reminder.
     * </p>
     *
     * @param serviceScheduleDTO
     */
    protected void postProcessing(ServiceScheduleDTO serviceScheduleDTO) {
        if (Objects.isNull(serviceScheduleDTO)) {
            log.error("Schedule is null to proceed with the reminder flow.");
            return;
        }
        eventPublisher.publish(SchedulingSuccessEvent.builder().eventData(serviceScheduleDTO).build());

    }

    private List<ServiceScheduleDTO> proceed(ServiceVehicleDTO serviceVehicleDTO) {
        return vehicleCoreScheduleEngine.schedule(serviceVehicleDTO);
    }

    protected abstract void preRequisite(ServiceVehicleDTO serviceVehicleDTO);

    protected boolean isActiveScheduleExists(ServiceVehicleDTO serviceVehicleDTO) {
        return vehicleServiceScheduleService.isActiveScheduleExists(serviceVehicleDTO.getRegNumber());
    }

    protected boolean isActiveReminderExists(ServiceVehicleDTO serviceVehicleDTO) {
        final var serviceReminders = activeReminders(serviceVehicleDTO);
        return Objects.nonNull(serviceReminders) && !serviceReminders.isEmpty();
    }

    protected List<ServiceReminderDTO> activeReminders(ServiceVehicleDTO serviceVehicleDTO) {
        return serviceReminderService.findActiveRemindersForVehicle(serviceVehicleDTO.getRegNumber());
    }
}
