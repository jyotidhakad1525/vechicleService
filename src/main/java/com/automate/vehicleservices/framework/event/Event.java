package com.automate.vehicleservices.framework.event;

import lombok.Data;

import java.util.UUID;

/**
 * marker Interface denoting Event.
 */
@Data
public class Event {
    private String eventId;

    public Event() {
        this.eventId = UUID.randomUUID().toString();
    }

}
