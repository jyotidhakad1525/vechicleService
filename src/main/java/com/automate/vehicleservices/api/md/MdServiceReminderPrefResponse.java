package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceReminderPref;
import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Data;

import java.util.Objects;

@Data
public class MdServiceReminderPrefResponse implements MdResponse {

    private int id;
    private int durationValue;
    private Expression expression;
    private TimeFrame timeFrame;
    private Boolean isActive;
    private int serviceTypeId;
    private int communicationModeId;
    private String serviceType;
    private CommunicationModeEnum communicationMode;

    public MdServiceReminderPrefResponse(MdServiceReminderPref mdServiceReminderPref) {
        if (Objects.isNull(mdServiceReminderPref))
            return;

        this.id = mdServiceReminderPref.getId();
        this.durationValue = mdServiceReminderPref.getDurationValue();
        this.expression = mdServiceReminderPref.getExpression();
        this.timeFrame = mdServiceReminderPref.getTimeFrame();
        this.isActive = mdServiceReminderPref.getIsActive();

        serviceType(mdServiceReminderPref);
        communicationMode(mdServiceReminderPref);
    }

    private void serviceType(MdServiceReminderPref mdServiceReminderPref) {
        final var mdServiceType = mdServiceReminderPref.getMdServiceType();
        if (Objects.nonNull(mdServiceType)) {
            this.serviceTypeId = mdServiceType.getId();
            this.serviceType = mdServiceType.getServiceName();
        }
    }

    private void communicationMode(MdServiceReminderPref mdServiceReminderPref) {
        final var communicationMode = mdServiceReminderPref.getCommunicationMode();
        if (Objects.nonNull(communicationMode)) {
            this.communicationModeId = communicationMode.getId();
            this.communicationMode = communicationMode.getType();
        }
    }
}
