package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.repository.dtoprojection.SlotAvailability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlotAvailabilityDTO {

    private LocalDate serviceDate;

    private int slotId;

    private String fromTime;

    private String toTime;

    private int allocated;

    private int available;

    private int booked;

    public SlotAvailabilityDTO(SlotAvailability slotAvailability, LocalDate serviceDate) {
        this.slotId = slotAvailability.getSlotID();
        this.fromTime = slotAvailability.getFromTime();
        this.toTime = slotAvailability.getToTime();
        this.allocated = slotAvailability.getAllocated();
        this.available = slotAvailability.getAvailable();
        this.serviceDate = serviceDate;
        this.booked = slotAvailability.getAllocated() - slotAvailability.getAvailable();
    }
}
