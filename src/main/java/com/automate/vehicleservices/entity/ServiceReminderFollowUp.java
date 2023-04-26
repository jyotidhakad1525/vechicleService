package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.FollowUpStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "service_reminder_follow_up")
public class ServiceReminderFollowUp extends Auditable implements Serializable {

    private static final long serialVersionUID = -424008267030679110L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private FollowUpStatus status;

    @JoinColumn(name = "REMINDER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceReminderDetails serviceReminderDetails;

    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn(name = "CRE_ASSIGNED")
    @ManyToOne
    private Employee cre;

    @OneToMany(mappedBy = "serviceReminderFollowUp", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH})
    @JsonManagedReference
    private List<ServiceReminderFollowUpActivity> serviceFollowUpActivities;

    @JoinColumn(name = "VEHICLE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceVehicle serviceVehicle;

    @JoinColumn(name = "TENANT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MdTenant tenant;

    public ServiceReminderFollowUpActivity addServiceFollowUpActivity(
            ServiceReminderFollowUpActivity serviceReminderFollowUpActivity) {
        getServiceFollowUpActivities().add(serviceReminderFollowUpActivity);
        serviceReminderFollowUpActivity.setServiceReminderFollowUp(this);

        return serviceReminderFollowUpActivity;
    }

    public ServiceReminderFollowUpActivity removeServiceFollowUpActivity(
            ServiceReminderFollowUpActivity serviceReminderFollowUpActivity) {
        getServiceFollowUpActivities().remove(serviceReminderFollowUpActivity);
        serviceReminderFollowUpActivity.setServiceReminderFollowUp(null);

        return serviceReminderFollowUpActivity;
    }

    public List<ServiceReminderFollowUpActivity> getServiceFollowUpActivities() {
        if (Objects.isNull(serviceFollowUpActivities))
            serviceFollowUpActivities = new ArrayList<>();
        return serviceFollowUpActivities;
    }

}

