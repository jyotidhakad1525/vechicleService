package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.CommunicationMode;
import com.automate.vehicleservices.entity.MdServiceReminderPref;
import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;

import java.time.LocalDateTime;

public final class MdServiceReminderPrefBuilder {
    private int id;
    private int durationValue;
    private Expression expression;
    private TimeFrame timeFrame;
    private Boolean isActive;
    private MdServiceType mdServiceType;
    private CommunicationMode communicationMode;
    private MdTenant mdTenant;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private MdServiceReminderPrefBuilder() {
    }

    public static MdServiceReminderPrefBuilder aMdServiceReminderPref() {
        return new MdServiceReminderPrefBuilder();
    }

    public MdServiceReminderPrefBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MdServiceReminderPrefBuilder withDurationValue(int durationValue) {
        this.durationValue = durationValue;
        return this;
    }

    public MdServiceReminderPrefBuilder withExpression(Expression expression) {
        this.expression = expression;
        return this;
    }

    public MdServiceReminderPrefBuilder withTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
        return this;
    }

    public MdServiceReminderPrefBuilder withIsActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public MdServiceReminderPrefBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }

    public MdServiceReminderPrefBuilder withCommunicationMode(CommunicationMode communicationMode) {
        this.communicationMode = communicationMode;
        return this;
    }

    public MdServiceReminderPrefBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceReminderPrefBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdServiceReminderPrefBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdServiceReminderPrefBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public MdServiceReminderPrefBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdServiceReminderPref build() {
        MdServiceReminderPref mdServiceReminderPref = new MdServiceReminderPref();
        mdServiceReminderPref.setId(id);
        mdServiceReminderPref.setDurationValue(durationValue);
        mdServiceReminderPref.setExpression(expression);
        mdServiceReminderPref.setTimeFrame(timeFrame);
        mdServiceReminderPref.setIsActive(isActive);
        mdServiceReminderPref.setMdServiceType(mdServiceType);
        mdServiceReminderPref.setCommunicationMode(communicationMode);
        mdServiceReminderPref.setMdTenant(mdTenant);
        mdServiceReminderPref.setCreatedBy(createdBy);
        mdServiceReminderPref.setCreatedDate(createdDate);
        mdServiceReminderPref.setLastModifiedBy(lastModifiedBy);
        mdServiceReminderPref.setLastModifiedDate(lastModifiedDate);
        return mdServiceReminderPref;
    }
}
