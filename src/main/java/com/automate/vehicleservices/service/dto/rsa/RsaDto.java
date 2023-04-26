package com.automate.vehicleservices.service.dto.rsa;

import com.automate.vehicleservices.entity.RSA;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RsaDto implements Serializable {

    private static final long serialVersionUID = 5789820298731682163L;

    private int id;
    private double amount;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdDate;

    private int customerId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date date;

    private String lastModifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date lastModifiedDate;
    private String reason;
    private String remarks;
    private String status;
    private RsaAddressDto rsaAddress;
    private String vehicleRegNo;

    public RsaDto(RSA rsa) {
        this.id = rsa.getId();
        this.amount = rsa.getAmount();
        this.createdBy = rsa.getCreatedBy();
        this.createdDate = rsa.getCreatedDate();
        this.customerId = rsa.getCustomerId();
        this.date = rsa.getDate();
        this.lastModifiedBy = rsa.getLastModifiedBy();
        this.lastModifiedDate = rsa.getLastModifiedDate();
        this.reason = rsa.getReason();
        this.remarks = rsa.getRemarks();
        this.status = rsa.getStatus();
        this.rsaAddress = new RsaAddressDto(rsa.getRsaAddress());
        this.vehicleRegNo = rsa.getVehicle().getRegNumber();
    }
}
