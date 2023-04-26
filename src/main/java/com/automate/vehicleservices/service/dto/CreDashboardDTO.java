package com.automate.vehicleservices.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreDashboardDTO {

    private String name;

    private Integer allocatedLeads;

    private Long attemptAchieved;

    private Integer attemptTarget;

    private Long contactAchieved;

    private Integer contactTarget;

    private Long bookAchieved;

    private Integer bookTarget;

    private Integer reportAchieved;

    private Integer reportTarget;
}
