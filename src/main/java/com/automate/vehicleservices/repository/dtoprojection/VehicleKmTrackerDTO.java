package com.automate.vehicleservices.repository.dtoprojection;

import java.time.LocalDate;

/**
 * @author Chandrashekar V
 */
public interface VehicleKmTrackerDTO {

    int getId();

    int getKmReading();

    LocalDate getRecordedDate();

}
