package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceSlot;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Data
@NoArgsConstructor
public class MdServiceSlotResponse implements MdResponse {

    private int id;
    private short availability;
    private LocalTime slotTimeFrom;
    private LocalTime slotTimeTo;
    private DayOfWeek day;
    private Boolean active;

    public MdServiceSlotResponse(MdServiceSlot serviceSlot) {
        if (Objects.isNull(serviceSlot))
            return;
        this.id = serviceSlot.getId();
        this.availability = serviceSlot.getAvailability();
        this.slotTimeFrom = serviceSlot.getSlotTimeFrom();
        this.slotTimeTo = serviceSlot.getSlotTimeTo();
        this.day = serviceSlot.getDay();
        this.active = serviceSlot.getActive();
    }
}
