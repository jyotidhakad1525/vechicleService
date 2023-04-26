package com.automate.vehicleservices.entity.enums;

/**
 * Vehicle Service Schedule Status.
 */
public enum ScheduleStatus {

    /**
     * NEW - New entry into service schedule IN_PROGRESS - Picked up for further processing COMPLETED - Schedule is
     * completed as per the plan. CANCELLED - Schedule is cancelled in between.
     */
    NEW,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
