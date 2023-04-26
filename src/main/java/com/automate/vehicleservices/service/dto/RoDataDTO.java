package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.RoDataDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoDataDTO {


    private String billNo;

    private String billDate;

    private String billType;


    private String roNumber;
    private String roDate;
    private String technician;
    private String serviceAdvisor;

    private Double totalBillAmount;

    public RoDataDTO(RoDataDetails details) {
        if (Objects.isNull(details))
            return;
        this.billNo = details.getBillNo();
        this.billDate = details.getBillDate();
        this.billType = details.getBillType();
        this.roNumber = details.getRoNumber();
        this.roDate = details.getRoDate();
        this.technician = details.getTechnician();
        this.serviceAdvisor = details.getServiceAdvisor();
        this.totalBillAmount = details.getTotalBillAmount();

    }

}
