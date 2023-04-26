package com.automate.vehicleservices.notification;

public interface NotificationStrategy<T extends NotificationData> {
    NotificationResponse notify(T notificationData);
}
