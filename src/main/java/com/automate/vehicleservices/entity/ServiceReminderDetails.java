package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * The persistent class for the service_reminder_history database table.
 */
@Getter
@Setter
@Entity
@Table(name = "service_reminder_details")
@NamedQuery(name = "ServiceReminderDetails.findAll", query = "SELECT s FROM ServiceReminderDetails s")
public class ServiceReminderDetails extends Auditable implements Serializable {

    private static final long serialVersionUID = -6768768915796918455L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "COMMUNICATION_ADDRESS", length = 45)
    private String communicationAddress;

    @Column(name = "DATE_OF_REMINDER")
    private LocalDate dateOfReminder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMUNICATION_MODE")
    @JsonBackReference
    private CommunicationMode communicationMode;

    @Column(name = "MESSAGE_BODY", length = 1000)
    private String messageBody;

    //bi-directional many-to-one association to ServiceReminder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_REMINDER")
    @JsonBackReference
    private ServiceReminder serviceReminder;

    @Column(name = "REMARKS", length = 1000)
    private String remarks;

    @Column(name = "IS_SUCCESS", columnDefinition = "TINYINT")
    private Boolean success;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

}