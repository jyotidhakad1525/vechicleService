package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceReminderFollowUpResultCapture;
import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceReminderFollowUpResultCaptureDTO {

    private int id;
    private String resultJsonData;
    private String createdBy;
    private LocalDateTime createdDate;
    private FollowUpActivityResult followUpActivityReason;

    public ServiceReminderFollowUpResultCaptureDTO(
            ServiceReminderFollowUpResultCapture serviceReminderFollowUpResultCapture) {
        if (Objects.isNull(serviceReminderFollowUpResultCapture))
            return;
        this.id = serviceReminderFollowUpResultCapture.getId();
        this.resultJsonData = serviceReminderFollowUpResultCapture.getResultJsonData();
        this.createdBy = serviceReminderFollowUpResultCapture.getCreatedBy();
        this.createdDate = serviceReminderFollowUpResultCapture.getCreatedDate();
        final var serviceReminderFollowUpActivity = serviceReminderFollowUpResultCapture
                .getServiceReminderFollowUpActivity();
        if (Objects.nonNull(serviceReminderFollowUpActivity)) {
            this.followUpActivityReason =
                    serviceReminderFollowUpActivity.getFollowUpActivityResult();
        }
    }
}
