package com.automate.vehicleservices.entity;


import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "round_robin_data_allocation_strategy")
public class RoundRobinDataAllocationStrategy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cre_id")
    private Employee employee;

    @OneToMany(mappedBy = "roundRobinDataAllocationStrategy", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ServiceTypeBasedAllocationRatio> allocationRatioList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private MdOrganization organization;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ActiveInActiveStatus status;

    @Column(name = "is_number",columnDefinition = "TINYINT")
    private Boolean isNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_datetime")
    private Date updatedDatetime;
}
