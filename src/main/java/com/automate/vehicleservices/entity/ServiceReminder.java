package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.ReminderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the service_reminder database table.
 */
@Setter
@Getter
@Entity
@Table(name = "service_reminder")
@NamedQuery(name = "ServiceReminder.findAll", query = "SELECT s FROM ServiceReminder s")
public class ServiceReminder extends Auditable implements Serializable {

    private static final long serialVersionUID = 3118893456032278879L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

    @Column(name = "REMIND_UNTIL")
    private LocalDate remindUntil;

    @Column(name = "REMINDER_START_DATE")
    private LocalDate reminderStartDate;

    @ManyToOne
    @JoinColumn(name = "VEHICLE_ID", nullable = false)
    private ServiceVehicle serviceVehicle;

    //bi-directional many-to-one association to MdServiceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_TYPE", nullable = false)
    @JsonBackReference
    private MdServiceType mdServiceType;

    //uni-directional many-to-one association to VehicleServiceSchedule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_SCHEDULE")
    private VehicleServiceSchedule vehicleServiceSchedule;

    //bi-directional many-to-one association to ServiceReminderHistory
    @OneToMany(mappedBy = "serviceReminder", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JsonManagedReference
    private List<ServiceReminderDetails> serviceReminderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private ReminderStatus reminderStatus;

    public ServiceReminderDetails addServiceReminderDetail(ServiceReminderDetails serviceReminderDetails) {
        getServiceReminderDetails().add(serviceReminderDetails);
        serviceReminderDetails.setServiceReminder(this);

        return serviceReminderDetails;
    }

    public ServiceReminderDetails removeServiceReminderDetail(ServiceReminderDetails serviceReminderDetails) {
        getServiceReminderDetails().remove(serviceReminderDetails);
        serviceReminderDetails.setServiceReminder(null);

        return serviceReminderDetails;
    }

    public List<ServiceReminderDetails> getServiceReminderDetails() {
        if (Objects.isNull(serviceReminderDetails))
            serviceReminderDetails = new ArrayList<>();
        return serviceReminderDetails;
    }
}