package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceSlotAvailability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Chandrashekar V
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Slot {

    private LocalTime from;
    private LocalTime to;
    private LocalDate date;
    private int id;

    public Slot(ServiceSlotAvailability slotAvailability) {
        this.from = slotAvailability.getMdServiceSlot().getSlotTimeFrom();
        this.to = slotAvailability.getMdServiceSlot().getSlotTimeTo();
        this.date = slotAvailability.getServiceDate();
        this.id = slotAvailability.getMdServiceSlot().getId();
    }
}
