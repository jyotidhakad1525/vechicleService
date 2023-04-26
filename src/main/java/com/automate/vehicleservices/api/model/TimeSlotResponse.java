package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.MdServiceSlot;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Chandrashekar V
 */
@Data
@NoArgsConstructor
public class TimeSlotResponse {
    private short availability;
    private LocalTime slotTimeFrom;
    private LocalTime slotTimeTo;
    private String tenantId;
    private DayOfWeek day;

    public TimeSlotResponse(@NotNull MdServiceSlot serviceSlot) {
        this.availability = serviceSlot.getAvailability();
        this.slotTimeFrom = serviceSlot.getSlotTimeFrom();
        this.slotTimeTo = serviceSlot.getSlotTimeTo();
        this.tenantId = serviceSlot.getMdTenant().getTenantIdentifier();
        this.day = serviceSlot.getDay();
    }
}
