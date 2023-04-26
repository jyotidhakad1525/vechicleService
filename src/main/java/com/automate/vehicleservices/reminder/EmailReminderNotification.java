package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.notification.EmailNotificationData;
import com.automate.vehicleservices.notification.NotificationData;
import com.automate.vehicleservices.notification.NotificationResponse;
import com.automate.vehicleservices.notification.NotificationStrategy;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import com.automate.vehicleservices.service.dto.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component("EmailReminderNotification")
@Slf4j
public class EmailReminderNotification extends AbstractReminderNotification {

    public static final String BODY_TEMPLATE = "<html><p>Dear <CUSTOMER_NAME>,<br>\n" +
            " <br>\n" +
            " Your car's <VEHICLE_REG_NUMBER>, service due date is on <SERVICE_DUE_DATE>. <br>\n" +
            " <br>\n" +
            " Please visit service center.<br>\n" +
            " <br>\n" +
            " - Team Hyundai</p></html>";
    public static final String SUBJECT = "Your car service is due!";
    @Autowired
    @Qualifier("EmailNotificationStrategy")
    private NotificationStrategy<EmailNotificationData> notificationStrategy;

    @Override
    protected NotificationData constructRequest(ReminderData reminderData) {
        LocalDate serviceDueDate = reminderData.getServiceDueDate();
        String customerName = reminderData.getCustomerName();
        String communicationAddress = reminderData.getCommunicationAddress();

        final String body = fillTheEmailBody(reminderData, serviceDueDate, customerName);

        return EmailNotificationData.builder().content(body).subject(SUBJECT).toAddress(new String[]{communicationAddress}).build();
    }

    private String fillTheEmailBody(ReminderData reminderData, LocalDate serviceDueDate,
                                    String customerName) {

        String afterReplacing = BODY_TEMPLATE
                .replace("<CUSTOMER_NAME>", customerName)
                .replace("<VEHICLE_REG_NUMBER>", reminderData.getRegNumber())
                .replace("<SERVICE_DUE_DATE>", serviceDueDate.toString());

        return afterReplacing;
    }

    @Override
    protected List<ValidationError> validate(ReminderData reminderData, ServiceReminderDetailsDTO reminderDetailsDTO) {
        List<ValidationError> errors = defaultValidation(reminderData);

        // Can do additional validation here, if required.
        final String communicationAddress = reminderData.getCommunicationAddress();
        final boolean validEmail = isValidEmail(communicationAddress);
        if (!validEmail)
            errors.add(ValidationError.builder()
                    .field("communicationAddress")
                    .error(String.format("Invalid Email address: %s", communicationAddress)).build());
        return errors;
    }

    private boolean isValidEmail(String communicationAddress) {
        return EmailValidator.getInstance().isValid(communicationAddress);
    }

    @Override
    protected NotificationStrategy notificationStrategy() {
        return notificationStrategy;
    }

    @Override
    protected boolean isSuccess(NotificationResponse response) {
        return Objects.nonNull(response) && StringUtils.equalsIgnoreCase("SUCCESS", response.getStatus());
    }
}
