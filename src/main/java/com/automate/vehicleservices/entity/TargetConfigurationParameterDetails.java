package com.automate.vehicleservices.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

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
@Entity
@Table(name = "target_parameter_details")
public class TargetConfigurationParameterDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "name")
    private String parameterName;

    @Column(name = "target")
    private Integer target;

    @Column(name = "unit")
    private Integer unit;

    @ManyToOne
    @JoinColumn(name = "target_id")
    @JsonBackReference
    private TargetConfigurationDetails targetConfigurationDetails;
}
