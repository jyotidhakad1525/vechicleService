package com.automate.vehicleservices.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_type_allocation_ratio")
public class ServiceTypeBasedAllocationRatio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private MdServiceType serviceTypes;

    @Column(name = "ratio")
    private int ratio;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "round_robin_allocation_id")
    private RoundRobinDataAllocationStrategy roundRobinDataAllocationStrategy;
}
