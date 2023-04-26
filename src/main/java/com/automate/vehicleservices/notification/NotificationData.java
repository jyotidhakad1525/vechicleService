package com.automate.vehicleservices.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationData {

    private String content;

    private String from;

    public NotificationData(String content, String from) {
        this.content = content;
        this.from = from;
    }
}
