package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="RSA")
@NamedQuery(name="RSA.findAll", query="SELECT r FROM RSA r")
@Getter
@Setter
public class RSA implements Serializable {
//public class RSA extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private double amount;

@Column(name="CREATED_BY")
private String createdBy;

@Temporal(TemporalType.TIMESTAMP)
@Column(name="CREATED_DATE")
private Date createdDate;

@Column(name="CUSTOMER_ID")
private int customerId;

@Temporal(TemporalType.TIMESTAMP)
private Date date;

@Column(name="LAST_MODIFIED_BY")
private String lastModifiedBy;

@Temporal(TemporalType.TIMESTAMP)
@Column(name="LAST_MODIFIED_DATE")
private Date lastModifiedDate;
	
private String reason;

private String remarks;

private String status;

//@Column(unique = true, nullable = false)

@Column(name="RSA_ID",unique = true, nullable = false)
private String rsaId;

@Column(name="BRANCH_ID")
private String branchId;

@Column(name="BRANCH_NAME")
private String branchName;

@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
@JoinColumn(name = "TECHNICIAN", referencedColumnName = "id", unique = true)
@JsonBackReference
private Employee technician;

@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
@JoinColumn(name = "VEHICLE", referencedColumnName = "id", unique = true)
@JsonBackReference
private ServiceVehicle vehicle;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "RSA_ADDRESS", referencedColumnName = "id", unique = true)
    @JsonBackReference
    private RSAAddress rsaAddress;

}