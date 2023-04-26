package com.automate.vehicleservices.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ro_data")
public class RoDataDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "service_center_location")
    private String serviceCenterLocation;

    @Column(name = "service_center_code")
    private String serviceCenterCode;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "bill_date")
    private String billDate;

    @Column(name = "bill_type")
    private String billType;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "vin")
    private String vin;

    @Column(name = "vehicle_reg_no")
    private String vehicleRegNo;

    @Column(name = "model")
    private String model;

    @Column(name = "ro_number")
    private String roNumber;

    @Column(name = "ro_date")
    private String roDate;

    @Column(name = "technician")
    private String technician;

    @Column(name = "service_advisor")
    private String serviceAdvisor;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "total_bill_amount")
    private Double totalBillAmount;

    @Column(name = "labour_amount")
    private Double labourAmount;

    @Column(name = "labour_tax")
    private Double labourTax;

    @Column(name = "part_amount")
    private Double partAmount;

    @Column(name = "part_tax")
    private Double partTax;
}
