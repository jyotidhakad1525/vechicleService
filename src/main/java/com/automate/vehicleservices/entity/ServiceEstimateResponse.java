package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.converter.HashMapConverter;
import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * The persistent class for the service_estimate_response database table.
 */
@Setter
@Getter
@Entity
@Table(name = "service_estimate_response")
@NamedQuery(name = "ServiceEstimateResponse.findAll", query = "SELECT s FROM ServiceEstimateResponse s")
public class ServiceEstimateResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "ADDITIONAL_SERVICES_TOTAL")
    private Double additionalServicesTotal;

    @Column(name = "APPROVED_BY", length = 255)
    private String approvedBy;

    @Column(name = "CREATED_BY", length = 255)
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "ESTIMATION_JSON", columnDefinition = "json")
    @Convert(converter = HashMapConverter.class)
    private Map<String, List<RateCardItem>> estimation;

    @Column(name = "ESTIMATION_SENT_TO_CUSTOMER")
    private byte estimationSentToCustomer;

    @Column(name = "LAST_MODIFIED_BY", length = 255)
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    private Double total;

    //bi-directional many-to-one association to ServiceEstimateRequest
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_REQUEST", nullable = false)
    @JsonBackReference
    private ServiceEstimate serviceEstimate;

}