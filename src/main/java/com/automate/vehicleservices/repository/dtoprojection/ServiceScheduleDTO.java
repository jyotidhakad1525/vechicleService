package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.VehicleServiceSchedule;
import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ServiceScheduleDTO {
    private LocalDate serviceDueDatePreferred;
    private LocalDate serviceDueDatePerSchedule;
    private LocalDate serviceDueDateRecommended;
    private ServiceVehicleDTO serviceVehicle;
    private int serviceType;
    private String serviceName;
    private String tenantIdentifier;
    private ScheduleStatus status;
    private int id;
    private LocalDateTime createdDate;

    public ServiceScheduleDTO(VehicleServiceSchedule vehicleServiceSchedule) {
        if (Objects.isNull(vehicleServiceSchedule))
            return;

        this.id = vehicleServiceSchedule.getId();
        this.serviceDueDatePreferred = vehicleServiceSchedule.getServiceDueDatePreferred();
        this.serviceDueDatePerSchedule = vehicleServiceSchedule.getServiceDueDatePerSchedule();
        this.serviceDueDateRecommended = vehicleServiceSchedule.getServiceDueDateRecommended();
        this.serviceVehicle = new ServiceVehicleDTO(vehicleServiceSchedule.getServiceVehicle());
        MdServiceType mdServiceType = vehicleServiceSchedule.getMdServiceType();
        if (Objects.nonNull(mdServiceType)) {
            this.serviceName = mdServiceType.getServiceName();
            this.serviceType = mdServiceType.getId();
        }
        this.tenantIdentifier = vehicleServiceSchedule.getMdTenant().getTenantIdentifier();
        this.status = vehicleServiceSchedule.getStatus();
        this.createdDate = vehicleServiceSchedule.getCreatedDate();
    }
}
