package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "RSA_ADDRESS", schema = "vehicle-services")
@NamedQuery(name = "RsaAddress.findAll", query = "SELECT r FROM RSAAddress r")
@Getter
@Setter
public class RSAAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String address;

    private String area;

    private String landmark;

    private float latitude;

    private float longitude;

    private String pin;

    @OneToOne(mappedBy = "rsaAddress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private RSA rSA;

}