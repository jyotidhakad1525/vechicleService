package com.automate.vehicleservices.repository.dtoprojection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chandrashekar V
 * <p>
 * Do not change order of declared variables. Because, Lombok generates all args constructor in the same sequence. And
 * the same sequence used in <code>@SqlResultSetMapping</code>.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateCardItem {
    private String shortCode;
    private String maintenanceItemName;
    private Double rate;
    private String serviceName;
    private int serviceTypeId;
    private String maintenanceType;
    private String category;
    private int categoryId;


}
