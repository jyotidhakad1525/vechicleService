package com.automate.vehicleservices.notification;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SMSNotificationData extends NotificationData {

    private String toAddress;

    @Builder
    public SMSNotificationData(String content, String from, String toAddress) {
        super(content, from);
        this.toAddress = toAddress;
    }
}
