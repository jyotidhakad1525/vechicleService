package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.framework.api.MdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class MdServiceSlotRequest extends MdRequest {
    @PositiveOrZero
    private short availability;
    @NotNull
    private LocalTime slotTimeFrom;
    @NotNull
    private LocalTime slotTimeTo;
    @NotNull
    private DayOfWeek day;
}
