package com.automate.vehicleservices.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_tenant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTenant extends Auditable implements Serializable {
    private static final long serialVersionUID = -4296371883354835854L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    //bi-directional many-to-one association to Customer
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonBackReference
    private Customer customer;

    //bi-directional many-to-one association to Customer
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TENANT_ID")
    @JsonBackReference
    private MdTenant mdTenant;
}
