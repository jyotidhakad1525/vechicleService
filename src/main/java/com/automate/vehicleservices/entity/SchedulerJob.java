package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.JobStatus;
import com.automate.vehicleservices.entity.enums.JobType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "scheduler_job")
public class SchedulerJob extends Auditable implements Serializable {
    private static final long serialVersionUID = 3922259209526237238L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "JOB_TYPE")
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private JobStatus status;

}
