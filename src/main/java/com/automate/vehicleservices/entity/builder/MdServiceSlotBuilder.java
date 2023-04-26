package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceSlot;
import com.automate.vehicleservices.entity.MdTenant;

import java.time.DayOfWeek;
import java.time.LocalTime;

public final class MdServiceSlotBuilder {
    private int id;
    private short availability;
    private LocalTime slotTimeFrom;
    private LocalTime slotTimeTo;
    private DayOfWeek day;
    private Boolean active;
    private MdTenant mdTenant;

    private MdServiceSlotBuilder() {
    }

    public static MdServiceSlotBuilder aMdServiceSlot() {
        return new MdServiceSlotBuilder();
    }

    public MdServiceSlotBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdServiceSlotBuilder withAvailability(short availability) {
        this.availability = availability;
        return this;
    }

    public MdServiceSlotBuilder withSlotTimeFrom(LocalTime slotTimeFrom) {
        this.slotTimeFrom = slotTimeFrom;
        return this;
    }

    public MdServiceSlotBuilder withSlotTimeTo(LocalTime slotTimeTo) {
        this.slotTimeTo = slotTimeTo;
        return this;
    }

    public MdServiceSlotBuilder withDay(DayOfWeek day) {
        this.day = day;
        return this;
    }

    public MdServiceSlotBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public MdServiceSlotBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceSlotBuilder but() {
        return aMdServiceSlot().withId(id).withAvailability(availability).withSlotTimeFrom(slotTimeFrom).withSlotTimeTo(slotTimeTo).withDay(day).withActive(active).withMdTenant(mdTenant);
    }

    public MdServiceSlot build() {
        MdServiceSlot mdServiceSlot = new MdServiceSlot();
        mdServiceSlot.setId(id);
        mdServiceSlot.setAvailability(availability);
        mdServiceSlot.setSlotTimeFrom(slotTimeFrom);
        mdServiceSlot.setSlotTimeTo(slotTimeTo);
        mdServiceSlot.setDay(day);
        mdServiceSlot.setActive(active);
        mdServiceSlot.setMdTenant(mdTenant);
        return mdServiceSlot;
    }
}
