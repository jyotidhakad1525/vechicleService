package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "service_follow_up_resulting_appointment")
@Entity
@Getter
@Setter
public class ServiceReminderFollowUpResultingAppointment extends CreateAuditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPOINTMENT_ID")
    private ServiceAppointment serviceAppointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWUP_ACTIVITY_ID")
    private ServiceReminderFollowUpActivity serviceReminderFollowUpActivity;

}
