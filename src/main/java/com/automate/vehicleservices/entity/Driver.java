package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "drivers", schema = "vehicle-services")
@NamedQuery(name = "Driver.findAll", query = "SELECT d FROM Driver d")
@Getter
@Setter
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "driver_type")
    private String driverType;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDatetime;

    @Column(name = "org_id")
    private Integer orgId;

    private String remarks;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;


    @OneToMany(mappedBy = "driver")
    private List<DriversAllotment> driversAllotments;

    public DriversAllotment addDriversAllotment(DriversAllotment driversAllotment) {
        getDriversAllotments().add(driversAllotment);
        driversAllotment.setDriver(this);

        return driversAllotment;
    }

    public DriversAllotment removeDriversAllotment(DriversAllotment driversAllotment) {
        getDriversAllotments().remove(driversAllotment);
        driversAllotment.setDriver(null);

        return driversAllotment;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

}