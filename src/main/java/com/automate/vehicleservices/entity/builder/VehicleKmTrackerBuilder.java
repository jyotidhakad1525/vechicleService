package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.VehicleKmTracker;

import java.time.LocalDateTime;

/**
 * @author Chandrashekar V
 */
public final class VehicleKmTrackerBuilder {
    private int kmReading;
    private String source;
    private LocalDateTime recordedDate;
    private ServiceVehicle serviceVehicle;

    private VehicleKmTrackerBuilder() {
    }

    public static VehicleKmTrackerBuilder aVehicleKmTracker() {
        return new VehicleKmTrackerBuilder();
    }

    public VehicleKmTrackerBuilder withKmReading(int kmReading) {
        this.kmReading = kmReading;
        return this;
    }

    public VehicleKmTrackerBuilder withSource(String source) {
        this.source = source;
        return this;
    }

    public VehicleKmTrackerBuilder withRecordedDate(LocalDateTime recordedDate) {
        this.recordedDate = recordedDate;
        return this;
    }

    public VehicleKmTrackerBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public VehicleKmTrackerBuilder but() {
        return aVehicleKmTracker().withKmReading(kmReading).withSource(source).withRecordedDate(recordedDate)
                .withServiceVehicle(serviceVehicle);
    }

    public VehicleKmTracker build() {
        VehicleKmTracker vehicleKmTracker = new VehicleKmTracker();
        vehicleKmTracker.setKmReading(kmReading);
        vehicleKmTracker.setSource(source);
        vehicleKmTracker.setRecordedDate(recordedDate);
        vehicleKmTracker.setServiceVehicle(serviceVehicle);
        return vehicleKmTracker;
    }
}
