package com.automate.vehicleservices.reminder;


import com.automate.vehicleservices.entity.ServiceReminder;
import com.automate.vehicleservices.event.NotificationEvent;
import com.automate.vehicleservices.framework.event.EventPublisher;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.notification.NotificationData;
import com.automate.vehicleservices.notification.NotificationResponse;
import com.automate.vehicleservices.notification.NotificationStrategy;
import com.automate.vehicleservices.reminder.event.ReminderReminderNotificationFailureEventData;
import com.automate.vehicleservices.reminder.event.ReminderReminderNotificationSuccessEventData;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import com.automate.vehicleservices.service.dto.ValidationError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class AbstractReminderNotification implements ReminderNotification {

    @Autowired
    private CrudService crudService;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    @Transactional
    public void notify(ServiceReminderDetailsDTO reminderDetailsDTO) {

        String error;
        ReminderData reminderData = null;
        try {

            reminderData = reminderData(reminderDetailsDTO);
            final List<ValidationError> validationErrors = validate(reminderData, reminderDetailsDTO);

            if (validationErrorsExists(reminderDetailsDTO, reminderData, validationErrors)) return;

            final NotificationData notificationData = constructRequest(reminderData);
            final var notificationResponse = notificationStrategy().notify(notificationData);

            processResponse(reminderDetailsDTO, reminderData, notificationData, notificationResponse);
            return;

        } catch (Exception ex) {
            error = ex.getMessage();
        }
        failureEvent(reminderDetailsDTO, reminderData, error);
    }

    public boolean validationErrorsExists(ServiceReminderDetailsDTO reminderDetailsDTO, ReminderData reminderData,
                                          List<ValidationError> validationErrors) {
        if (Objects.nonNull(validationErrors) && !validationErrors.isEmpty()) {
            // errors exists.
            final String message = Arrays.toString(validationErrors.toArray());
            log.error(String.format("Errors exist to send notification: %s ", message));

            failureEvent(reminderDetailsDTO, reminderData, message);
            return true;
        }
        return false;
    }

    public void processResponse(ServiceReminderDetailsDTO reminderDetailsDTO, ReminderData reminderData,
                                NotificationData notificationData, NotificationResponse response) {
        boolean isSuccess = isSuccess(response);
        if (isSuccess) {
            constructSuccessEvent(reminderDetailsDTO, reminderData, notificationData);
            return;
        }
        try {
            String error = new ObjectMapper().writeValueAsString(response);
            failureEvent(reminderDetailsDTO, reminderData, error);
            return;
        } catch (JsonProcessingException e) {
            log.error("Exception while parsing the API error response", e);
        }
        failureEvent(reminderDetailsDTO, reminderData, "Exception while parsing the API error response");
    }

    /**
     * Validation applicable for both (Email & SMS) implementations.
     *
     * @param reminderData
     * @return
     */
    public List<ValidationError> defaultValidation(ReminderData reminderData) {
        final LocalDate serviceDueDate = reminderData.getServiceDueDate();
        final String communicationAddress = reminderData.getCommunicationAddress();

        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isBlank(communicationAddress)) {
            errors.add(ValidationError.builder()
                    .field("communicationAddress")
                    .error("No Communication address found").build());
        }

        if (Objects.isNull(serviceDueDate)) {
            log.error("No service due date found.Can not send email");
            errors.add(ValidationError.builder()
                    .field("serviceDueDate")
                    .error("No service due date found")
                    .build());
        }
        return errors;
    }


    public void constructSuccessEvent(ServiceReminderDetailsDTO reminderDetailsDTO,
                                      ReminderData reminderData,
                                      NotificationData notificationData) {
        final var successNotificationEventData =
                ReminderReminderNotificationSuccessEventData.SuccessNotificationEventDataBuilder.aSuccessNotificationEventData()
                        .withServiceReminderDetailsDTO(reminderDetailsDTO)
                        .withCustomer(reminderData.getCustomerName())
                        .withVehicleRegNumber(reminderData.getRegNumber())
                        .withServiceDate(reminderData.getServiceDueDate())
                        .withMessageBody(notificationData.getContent())
                        .build();

        eventPublisher.publish(NotificationEvent.builder()
                .reminderNotificationEventData(successNotificationEventData).build());
    }

    public void failureEvent(ServiceReminderDetailsDTO reminderDetailsDTO,
                             ReminderData reminderData, final String message) {
        final ReminderReminderNotificationFailureEventData.FailureNotificationEventDataBuilder failureNotificationEventDataBuilder =
                ReminderReminderNotificationFailureEventData.FailureNotificationEventDataBuilder.aFailureNotificationEventData()
                        .withMessage(message)
                        .withServiceReminderDetailsDTO(reminderDetailsDTO);
        if (Objects.nonNull(reminderData))
            failureNotificationEventDataBuilder.withCustomer(reminderData.getCustomerName())
                    .withVehicleRegNumber(reminderData.getRegNumber())
                    .withServiceDate(reminderData.getServiceDueDate());


        final var failureNotificationEventData = failureNotificationEventDataBuilder.build();
        eventPublisher.publish(NotificationEvent.builder()
                .reminderNotificationEventData(failureNotificationEventData).build());
    }

    protected abstract List<ValidationError> validate(ReminderData reminderData,
                                                      ServiceReminderDetailsDTO reminderDetailsDTO);

    protected abstract NotificationData constructRequest(ReminderData reminderData);

    protected abstract boolean isSuccess(NotificationResponse response);

    public ReminderData reminderData(ServiceReminderDetailsDTO reminderDetailsDTO) {

        final var serviceReminderId = reminderDetailsDTO.getServiceReminderId();
        final var serviceReminderOpt = crudService.findById(serviceReminderId, ServiceReminder.class);

        if (serviceReminderOpt.isEmpty()) {
            log.error("No Reminder information found. Can not send email");
            return null;
        }

        return new ReminderData(reminderDetailsDTO, serviceReminderOpt.get()).invoke();
    }

    protected abstract NotificationStrategy notificationStrategy();

}
