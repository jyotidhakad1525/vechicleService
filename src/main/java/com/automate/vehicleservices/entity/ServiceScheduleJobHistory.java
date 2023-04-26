package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.VehicleJobStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "service_schedule_job_history")
public class ServiceScheduleJobHistory extends Auditable implements Serializable {

    private static final long serialVersionUID = -3246399140005790275L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    //bi-directional many-to-one association to ServiceVehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_ID", nullable = false)
    private ServiceVehicle serviceVehicle;

    //bi-directional many-to-one association to ServiceVehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_ID", nullable = false)
    private SchedulerJob schedulerJob;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private VehicleJobStatus status;

    @Column(name = "FAILURE_REASON")
    private String failureReason;
}
