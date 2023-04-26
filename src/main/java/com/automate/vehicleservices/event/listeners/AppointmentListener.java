package com.automate.vehicleservices.event.listeners;

import com.automate.vehicleservices.event.AppointmentBookedEvent;
import com.automate.vehicleservices.notification.EmailNotificationData;
import com.automate.vehicleservices.notification.NotificationStrategy;
import com.automate.vehicleservices.notification.SMSNotificationData;
import com.automate.vehicleservices.service.dto.ServiceAppointmentResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Appointment listener.
 */
@Component
@Slf4j
public class AppointmentListener {

    @Autowired
    private NotificationStrategy<SMSNotificationData> smsNotificationDataNotificationStrategy;

    @Autowired
    private NotificationStrategy<EmailNotificationData> emailNotificationDataNotificationStrategy;

    @EventListener(value = {AppointmentBookedEvent.class})
    @Async
    public void initOfflineFollowUp(AppointmentBookedEvent appointmentBookedEvent) {
        final var serviceAppointmentResponse = appointmentBookedEvent.getServiceAppointmentResponse();

        if (StringUtils.isNotBlank(serviceAppointmentResponse.getContactNumber()))
            sendSMS(serviceAppointmentResponse);

        if (StringUtils.isNotBlank(serviceAppointmentResponse.getEmail()))
            sendEmail(serviceAppointmentResponse);

    }

    private void sendEmail(ServiceAppointmentResponse serviceAppointmentResponse) {
        EmailNotificationData emailNotificationData = EmailNotificationData.builder()
                .subject("Service Appointment Communication")
                .toAddress(new String[]{serviceAppointmentResponse.getEmail()})
                .content(emailContent(serviceAppointmentResponse))
                .build();
        final var response = emailNotificationDataNotificationStrategy.notify(emailNotificationData);

        // Handle response
        if (Objects.isNull(response))
            log.error("Email API response is empty. Notification may have been failed.");
        else
            log.info(String.format("EMAIL API response -  %s", response.toString())); // TODO - Write to DB?

    }

    private String emailContent(ServiceAppointmentResponse serviceAppointmentResponse) {
        StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append("Dear ").append(serviceAppointmentResponse.getFirstName()).append(",").append(StringUtils.SPACE).append("<br>")
                .append(" Your vehicle - ").append(serviceAppointmentResponse.getVehicleRegNumber()).append(StringUtils.SPACE)
                .append("Appointment Booking Status: ").append(serviceAppointmentResponse.getServiceAppointmentStatus().toString()).append(".").append(StringUtils.SPACE)
                .append("Service Date: ").append(serviceAppointmentResponse.getServiceDate()).append(StringUtils.EMPTY).append(",").append("<br>")
                .append("Service Name: ").append(serviceAppointmentResponse.getServiceName()).append(StringUtils.EMPTY).append(",").append("<br>")
                .append("Time: ").append(serviceAppointmentResponse.getSlot().getFrom()).append(StringUtils.EMPTY).append(".").append("<br>");
        // TODO : Proper template, email signature, etc.. need rto be considered.
        return emailBuilder.toString();
    }

    private void sendSMS(ServiceAppointmentResponse serviceAppointmentResponse) {
        SMSNotificationData smsNotificationData = SMSNotificationData.builder()
                .content(smsContent(serviceAppointmentResponse))
                .toAddress(serviceAppointmentResponse.getContactNumber())
                .build();

        final var response = smsNotificationDataNotificationStrategy.notify(smsNotificationData);

        if (Objects.isNull(response))
            log.error("SMS API response is empty. Notification may have been failed.");
        else
            log.info(String.format("SMS API response -  %s", response.toString())); // TODO - Write to DB?

    }

    private String smsContent(ServiceAppointmentResponse serviceAppointmentResponse) {

        StringBuilder smsBuilder = new StringBuilder();
        smsBuilder.append("Dear ").append(serviceAppointmentResponse.getFirstName()).append(",").append(StringUtils.SPACE)
                .append(" Your vehicle - ").append(serviceAppointmentResponse.getVehicleRegNumber()).append(StringUtils.SPACE)
                .append("Appointment Booking Status: ").append(serviceAppointmentResponse.getServiceAppointmentStatus().toString()).append(".").append(StringUtils.SPACE)
                .append("Service Date: ").append(serviceAppointmentResponse.getServiceDate()).append(StringUtils.EMPTY).append(",")
                .append("Service Name: ").append(serviceAppointmentResponse.getServiceName()).append(StringUtils.EMPTY).append(",")
                .append("Time: ").append(serviceAppointmentResponse.getSlot().getFrom()).append(StringUtils.EMPTY).append(".");
        // TODO : Proper template, sender's signature, etc.. need to be considered.
        return smsBuilder.toString();


    }


}
