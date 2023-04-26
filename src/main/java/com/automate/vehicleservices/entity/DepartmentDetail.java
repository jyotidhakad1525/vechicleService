package com.automate.vehicleservices.entity;


import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "department_detail")
public class DepartmentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "department_value")
    private int departmentValue;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ActiveInActiveStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_datetime")
    private Date updatedDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private MdOrganization organization;

    @OneToMany(mappedBy = "departmentDetail")
    @JsonManagedReference
    private List<DesignationDetail> designationDetail;

    @OneToMany(mappedBy = "departmentDetail")
    @JsonManagedReference
    private List<TargetConfigurationDetails> targetConfigurationDetails;
}
