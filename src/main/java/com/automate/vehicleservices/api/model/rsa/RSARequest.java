package com.automate.vehicleservices.api.model.rsa;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSARequest implements Serializable {

    private static final long serialVersionUID = -4089000162580531243L;

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

    private RSAAddressRequest rsaAddressRequest;

    private String branchName;

    private int technician;

   private String vehicleRegNo;

}
