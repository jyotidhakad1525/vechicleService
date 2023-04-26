package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.notification.NotificationData;
import com.automate.vehicleservices.notification.NotificationResponse;
import com.automate.vehicleservices.notification.NotificationStrategy;
import com.automate.vehicleservices.notification.SMSNotificationData;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import com.automate.vehicleservices.service.dto.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(value = "SMSlReminderNotification")
@Slf4j
public class SMSlReminderNotification extends AbstractReminderNotification {

    public static final String BODY_TEMPLATE = "Hey <CUSTOMER_NAME>" +
            " Your car's <VEHICLE_REG_NUMBER> service due date is on <SERVICE_DUE_DATE>." +
            " Why dont we setup an appointment and get this done right away?" +
            " - Team Hyundai";

    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final String PHONE_NUMBER_INDIAN = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
    public static final String NON_NUMBER_VALUES = "[^0-9]";
    @Autowired
    @Qualifier("SMSNotificationStrategy")
    private NotificationStrategy<SMSNotificationData> notificationStrategy;

    @Override
    protected NotificationData constructRequest(ReminderData reminderData) {
        LocalDate serviceDueDate = reminderData.getServiceDueDate();
        String customerName = reminderData.getCustomerName();
        String communicationAddress = getValidPhoneNumber(reminderData.getCommunicationAddress());

        final String body = fillTheSMSBody(reminderData, serviceDueDate, customerName);
        return SMSNotificationData.builder().content(body).toAddress(communicationAddress).build();
    }

    @Override
    protected List<ValidationError> validate(ReminderData reminderData, ServiceReminderDetailsDTO reminderDetailsDTO) {
        List<ValidationError> errors = defaultValidation(reminderData);

        // Can do additional validation here, if required.

        // SMS validation
        final String communicationAddress = reminderData.getCommunicationAddress();
        final var s = getValidPhoneNumber(communicationAddress);
        if (StringUtils.isBlank(s))
            errors.add(ValidationError.builder().field("communicationAddress").error(String.format("Invalid phone " +
                    "number :%s", communicationAddress)).build());
        return errors;
    }

    /**
     * If given one is valid format returns 10 digit number, if not returns null.
     *
     * @param communicationAddress
     * @return
     */
    private String getValidPhoneNumber(String communicationAddress) {
        final String trim = StringUtils.trim(communicationAddress);
        if (StringUtils.isBlank(trim))
            return null;

        final String afterRemovingNonNumbers = trim.replaceAll(NON_NUMBER_VALUES, StringUtils.EMPTY);

        Pattern p = Pattern.compile(PHONE_NUMBER_INDIAN);
        Matcher m = p.matcher(afterRemovingNonNumbers);
        if (m.find()) {
            if (trim.length() >= PHONE_NUMBER_LENGTH) {
                return afterRemovingNonNumbers.substring(afterRemovingNonNumbers.length() - PHONE_NUMBER_LENGTH);
            }
        }

        return null;
    }

    @Override
    protected NotificationStrategy notificationStrategy() {
        return notificationStrategy;
    }

    public String fillTheSMSBody(ReminderData reminderData, LocalDate serviceDueDate,
                                 String customerName) {

        BODY_TEMPLATE.replace("<CUSTOMER_NAME>", customerName).replace("<VEHICLE_REG_NUMBER>",
                reminderData.getRegNumber()).replace("<SERVICE_DUE_DATE>", serviceDueDate.toString());

        return BODY_TEMPLATE;
    }

    @Override
    protected boolean isSuccess(NotificationResponse response) {
        return Objects.nonNull(response) && StringUtils.equalsIgnoreCase("SUCCESS", response.getStatus());
    }

}
