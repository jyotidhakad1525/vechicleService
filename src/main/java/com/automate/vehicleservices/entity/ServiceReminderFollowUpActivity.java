package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;
import com.automate.vehicleservices.entity.enums.FollowUpActivityStatus;
import com.automate.vehicleservices.entity.enums.FollowUpReason;
import com.automate.vehicleservices.entity.enums.FollowUpStepStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "service_follow_up_activity")
public class ServiceReminderFollowUpActivity extends Auditable implements Serializable {

    private static final long serialVersionUID = -5883846929854666625L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @JoinColumn(name = "FOLLOW_UP_ID")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
    private ServiceReminderFollowUp serviceReminderFollowUp;

    @JoinColumn(name = "CRE", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee cre;

    @Column(name = "CRE_REMARKS")
    private String creRemarks;

    @Column(name = "CUSTOMER_REMARKS")
    private String customerRemarks;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "FOLLOW_UP_DATE")
    private LocalDateTime followUpDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "ro_id")
    private RoDataDetails roData;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private FollowUpActivityStatus followUpActivityStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESULT", nullable = false)
    private FollowUpActivityResult followUpActivityResult;

    @Enumerated(EnumType.STRING)
    @Column(name = "REASON", nullable = false)
    private FollowUpReason followUpReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "FOLLOW_UP_STATUS")
    private FollowUpStepStatus followUpStatus;

    @OneToOne(mappedBy = "serviceReminderFollowUpActivity", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH})
    @JsonManagedReference
    private ServiceReminderFollowUpResultCapture serviceReminderFollowUpResultCapture;

    public ServiceReminderFollowUpResultCapture addServiceFollowUpResultCapture(
            ServiceReminderFollowUpResultCapture serviceReminderFollowUpResultCapture) {
        this.serviceReminderFollowUpResultCapture = serviceReminderFollowUpResultCapture;
        serviceReminderFollowUpResultCapture.setServiceReminderFollowUpActivity(this);
        return serviceReminderFollowUpResultCapture;
    }

    public ServiceReminderFollowUpResultCapture removeServiceFollowUpResultCapture(
            ServiceReminderFollowUpResultCapture serviceReminderFollowUpResultCapture) {
        this.serviceReminderFollowUpResultCapture = null;
        serviceReminderFollowUpResultCapture.setServiceReminderFollowUpActivity(null);
        return serviceReminderFollowUpResultCapture;
    }

}