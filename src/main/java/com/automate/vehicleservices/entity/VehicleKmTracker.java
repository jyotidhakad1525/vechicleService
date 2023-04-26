package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The persistent class for the vehicle_km_tracker database table.
 */
@Getter
@Setter
@Entity
@Table(name = "vehicle_km_tracker")
@NamedQuery(name = "VehicleKmTracker.findAll", query = "SELECT v FROM VehicleKmTracker v")
//@EntityListeners(VehicleKmTrackerEntityListener.class)
public class VehicleKmTracker implements Serializable {
    private static final long serialVersionUID = 7836546374696730917L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "KM_READING", nullable = false)
    private int kmReading;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "RECORDED_DATE", nullable = false)
    private LocalDateTime recordedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_ID")
    @JsonBackReference
    private ServiceVehicle serviceVehicle;

    @Override
    public String toString() {
        return "VehicleKmTracker{" +
                "id=" + id +
                ", kmReading=" + kmReading +
                ", source='" + source + '\'' +
                ", recordedDate=" + recordedDate +
                ", serviceVehicle=" + serviceVehicle.getRegNumber() +
                '}';
    }
}