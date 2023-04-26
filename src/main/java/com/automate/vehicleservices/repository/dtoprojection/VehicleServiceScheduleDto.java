package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.builder.MdServiceTypeBuilder;
import com.automate.vehicleservices.entity.builder.MdTenantBuilder;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class VehicleServiceScheduleDto {
    private int id;
    private String lastModifiedReason;
    private LocalDate serviceDueDatePreferred;
    private LocalDate serviceDueDatePerSchedule;
    private LocalDate serviceDueDateRecommended;
    private LocalDate lastServiceDate;
    private ServiceVehicleDTO serviceVehicle;
    private MdServiceTypeBuilder mdServiceType;
    private MdTenantBuilder mdTenant;
    private ScheduleStatus status;
    private int kmBetweenLastTwoServices;
    private int differenceInDaysBetweenLastTwoServices;
    private Float averageKM;
    private String serviceCategory;
}
