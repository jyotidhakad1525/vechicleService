package com.automate.vehicleservices.schedule.v1;

import com.automate.vehicleservices.event.CancelServiceReminderEvent;
import com.automate.vehicleservices.event.CancelServiceScheduleEvent;
import com.automate.vehicleservices.event.VehicleEventData;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.service.dto.ServiceReminderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component("UpdatedVehicleSchedulingEngineV1")
public class UpdatedVehicleSchedule extends AbstractVehicleSchedule {

    @Override
    protected void preRequisite(ServiceVehicleDTO serviceVehicleDTO) {

        if (isActiveScheduleExists(serviceVehicleDTO)) {
            log.info(String.format("Active schedule exists for vehicle %s", serviceVehicleDTO.getRegNumber()));
            /* Fire Cancel Reminder event. Cancel reminder in turn fires cancel schedule event. So that the vehicle
            is eligible for scheduling again. */
            cancelReminderIfExistsAndActive(serviceVehicleDTO);
        } else { // failing any one of the above conditions would mean to cancel the schedule directly.
            final var serviceScheduleCancelEvent = CancelServiceScheduleEvent.builder()
                    .eventData(new VehicleEventData(serviceVehicleDTO)).build();
            eventPublisher.publish(serviceScheduleCancelEvent);
        }

    }

    /**
     * When vehicle is updated if there is any active reminder exists, the same needs to be cancelled.
     *
     * @param serviceVehicleDTO
     */
    private void cancelReminderIfExistsAndActive(ServiceVehicleDTO serviceVehicleDTO) {
        final var serviceReminders = activeReminders(serviceVehicleDTO);

        if (Objects.isNull(serviceReminders) && serviceReminders.isEmpty())
            return;

        if (serviceReminders.size() > 1)
            log.warn("Only one reminder is supposed to be active. Corrupted data. Invalidating /Cancelling all " +
                    "reminders.");

        final var serviceReminderIds =
                serviceReminders.stream().map(ServiceReminderDTO::getId).collect(Collectors.toList());

        final var cancelServiceReminderEvent = CancelServiceReminderEvent.builder()
                .eventData(new VehicleEventData(serviceVehicleDTO))
                .reminderIds(serviceReminderIds)
                .build();

        eventPublisher.publish(cancelServiceReminderEvent);
    }


}
