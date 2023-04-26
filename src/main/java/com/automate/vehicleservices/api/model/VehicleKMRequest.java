package com.automate.vehicleservices.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VehicleKMRequest {

    @PositiveOrZero
    private int kmReading;
    private String source;
    private LocalDateTime recordedDate;

    public VehicleKMRequest(int kmReading, String source, LocalDateTime recordedDate) {
        this.kmReading = kmReading;
        this.source = source;
        if (null == recordedDate)
            this.recordedDate = LocalDateTime.now();
    }

    public VehicleKMRequest(int kmReading, String source) {
        this.kmReading = kmReading;
        this.source = source;
        this.recordedDate = LocalDateTime.now();
    }
}
