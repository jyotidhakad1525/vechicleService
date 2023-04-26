package com.automate.vehicleservices.framework.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publishes vehicle schedule event
     *
     * @param event
     */
    public void publish(Event event) {
        log.info(String.format("Publishing event:  %s", event.toString()));
        applicationEventPublisher.publishEvent(event);
    }
}
