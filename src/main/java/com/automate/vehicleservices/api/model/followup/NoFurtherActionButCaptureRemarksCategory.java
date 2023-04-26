package com.automate.vehicleservices.api.model.followup;

import lombok.Data;

@Data
public class NoFurtherActionButCaptureRemarksCategory extends FollowUpResultCapture {


    public NoFurtherActionButCaptureRemarksCategory(String reason, String creRemarks) {
        super(reason, creRemarks);
    }

    public NoFurtherActionButCaptureRemarksCategory() {
    }
}
