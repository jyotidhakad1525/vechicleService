package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.CommunicationMode;
import com.automate.vehicleservices.entity.MdServiceReminderPref;
import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ServiceReminderPrefDTO {
    private int durationValue;
    private Expression expression;
    private TimeFrame timeFrame;
    private int serviceType;
    private CommunicationMode communicationMode;
    private int id;

    public ServiceReminderPrefDTO(final MdServiceReminderPref mdServiceReminderPref) {
        this.communicationMode = mdServiceReminderPref.getCommunicationMode();
        this.durationValue = mdServiceReminderPref.getDurationValue();
        this.expression = mdServiceReminderPref.getExpression();
        this.serviceType = mdServiceReminderPref.getMdServiceType().getId();
        this.timeFrame = mdServiceReminderPref.getTimeFrame();
        this.id = mdServiceReminderPref.getId();
    }


}
