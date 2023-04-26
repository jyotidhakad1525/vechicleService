package com.automate.vehicleservices.api.model.followup;

import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;
import com.automate.vehicleservices.entity.enums.FollowUpActivityStatus;
import com.automate.vehicleservices.entity.enums.FollowUpStepStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpUpdateRequest {

    @Positive
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int assignCRE;
    private String creRemarks;
    private String customerRemarks;
    private FollowUpStepStatus status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime followUpDate;
    private FollowUpResultCapture followUpResultCapture; //TODO - Multiple result captures are possible based on how
    //  the service is closed.

    @NotNull
    private FollowUpActivityStatus followUpActivityStatus;

    @NotNull
    private FollowUpActivityResult followUpActivityResult;

    @Positive
    private int followUpId;

    public LocalDateTime getFollowUpDate() {
        if (Objects.isNull(this.followUpDate))
            this.followUpDate = LocalDateTime.now();
        return followUpDate;
    }

}
