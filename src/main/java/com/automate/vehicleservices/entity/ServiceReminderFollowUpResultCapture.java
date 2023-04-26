package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "service_follow_up_result_capture")
@Entity
@Getter
@Setter
public class ServiceReminderFollowUpResultCapture extends CreateAuditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "FOLLOW_UP_ACTIVITY_ID")
    @JsonBackReference
    private ServiceReminderFollowUpActivity serviceReminderFollowUpActivity;

    @Column(name = "RESULT_JSON", columnDefinition = "json")
    private String resultJsonData;


}
