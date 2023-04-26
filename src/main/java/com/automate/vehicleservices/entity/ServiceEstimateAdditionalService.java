package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the service_appointment_additional_services database table.
 */
@Setter
@Getter
@Entity
@Table(name = "service_estimate_additional_services")
@NamedQuery(name = "ServiceEstimateAdditionalService.findAll", query = "SELECT s FROM " +
        "ServiceEstimateAdditionalService s")
public class ServiceEstimateAdditionalService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    //uni-directional many-to-one association to MdServiceItem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDITIONAL_SERVICE")
    private MdServiceItem mdServiceItem;

    //bi-directional many-to-one association to ServiceAppointment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_REQUEST_ID")
    @JsonBackReference
    private ServiceEstimate serviceEstimate;

}