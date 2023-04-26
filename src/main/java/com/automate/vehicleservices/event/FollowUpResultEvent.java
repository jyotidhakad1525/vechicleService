package com.automate.vehicleservices.event;

import com.automate.vehicleservices.api.model.followup.FollowUpResultCapture;
import com.automate.vehicleservices.framework.event.Event;
import com.automate.vehicleservices.service.dto.ServiceReminderFollowUpActivityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUpResultEvent extends Event {

    private ServiceReminderFollowUpActivityDTO serviceReminderFollowUpActivityDTO;
    private FollowUpResultCapture followUpResultCapture;


}
