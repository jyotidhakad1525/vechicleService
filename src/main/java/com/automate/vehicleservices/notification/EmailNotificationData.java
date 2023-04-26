package com.automate.vehicleservices.notification;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailNotificationData extends NotificationData {

    private String subject;

    private String[] to;

    @Builder
    public EmailNotificationData(String content, String subject, String from, String[] toAddress) {
        super(content, from);
        this.subject = subject;
        this.to = toAddress;
    }
}
