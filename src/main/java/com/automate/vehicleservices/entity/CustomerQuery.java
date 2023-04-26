package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryDepartment;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryPurpose;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "customer_query")
public class CustomerQuery extends Auditable implements Serializable {

    private static final long serialVersionUID = -424008267030679110L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private String id;

    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "CRE_ASSIGNED")
    private String assignedTo;

    @Column(name = "ACTION_PLAN")
    private String actionPlan;

    @Column(name = "CRE_REMARKS")
    private String creRemarks;

    @Column(name = "QUERY", nullable = false)
    private String query;

    @JoinColumn(name = "VEHICLE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceVehicle serviceVehicle;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENQUIRY_DEPARTMENT", nullable = false)
    private CustomerQueryEnquiryDepartment customerQueryEnquiryDepartment;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private CustomerQueryEnquiryStatus customerQueryEnquiryStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "PURPOSE", nullable = false)
    private CustomerQueryEnquiryPurpose customerQueryEnquiryPurpose;
}

