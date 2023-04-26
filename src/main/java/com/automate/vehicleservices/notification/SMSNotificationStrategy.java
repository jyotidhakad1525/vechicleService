package com.automate.vehicleservices.notification;

import com.automate.vehicleservices.outbound.OutboundCaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("SMSNotificationStrategy")
public class SMSNotificationStrategy implements NotificationStrategy<SMSNotificationData> {

    private final OutboundCaller outboundCaller;
    @Value("${dms.vehicle-services.notifications.service.sms-api}")
    private String smsAPI;

    public SMSNotificationStrategy(OutboundCaller outboundCaller) {
        this.outboundCaller = outboundCaller;
    }

    @Override
    public NotificationResponse notify(SMSNotificationData notificationData) {

        SMSAPIRequest smsapiRequest =
                SMSAPIRequest.builder()
                        .message(notificationData.getContent())
                        .phoneNumber(notificationData.getToAddress())
                        .build();
        final var response = outboundCaller.post(smsAPI, null, smsapiRequest,
                SMSAPIResponse.class, null);

        if (Objects.nonNull(response) && response instanceof SMSAPIResponse)
            return (SMSAPIResponse) response;

        return null;
    }
}
