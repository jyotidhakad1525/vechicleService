package com.automate.vehicleservices.api.model.followup;

import com.automate.vehicleservices.entity.enums.FollowUpReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    private FollowUpReason followUpReason;

    @Positive
    private int assignCRE;
    private String creRemarks;
    private String customerRemarks;
    @Positive
    private int followUpId;

}
