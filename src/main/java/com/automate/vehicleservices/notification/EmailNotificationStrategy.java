package com.automate.vehicleservices.notification;


import com.automate.vehicleservices.outbound.OutboundCaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("EmailNotificationStrategy")
public class EmailNotificationStrategy implements NotificationStrategy<EmailNotificationData> {

    protected final OutboundCaller outboundCaller;
    @Value("${dms.vehicle-services.notifications.service.email-api}")
    private String emailAPI;

    public EmailNotificationStrategy(OutboundCaller outboundCaller) {
        this.outboundCaller = outboundCaller;
    }

    @Override
    public NotificationResponse notify(EmailNotificationData notificationData) {

        EmailAPIRequest emailAPIRequest =
                EmailAPIRequest.builder()
                        .content(notificationData.getContent())
                        .contentType("HTML")
                        .from(notificationData.getFrom())
                        .to(notificationData.getTo())
                        .build();
        final var response = outboundCaller.post(emailAPI, null, emailAPIRequest,
                EmailAPIResponse.class, null);

        if (Objects.nonNull(response) && response instanceof EmailAPIResponse)
            return (EmailAPIResponse) response;

        return null;
    }
}
