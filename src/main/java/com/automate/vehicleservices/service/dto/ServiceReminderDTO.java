package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceReminder;
import com.automate.vehicleservices.entity.ServiceReminderDetails;
import com.automate.vehicleservices.entity.enums.ReminderStatus;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceTypeDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServiceReminderDTO {
    private String createdBy;
    private LocalDateTime createdDate;
    private boolean active;
    private String modifiedBy;
    private LocalDate remindUntil;
    private LocalDate reminderStartDate;
    private ServiceVehicleDTO serviceVehicle;
    private ServiceTypeDTO serviceType;
    private ServiceScheduleDTO vehicleServiceSchedule;
    private List<ServiceReminderDetailsDTO> serviceReminderDetails = new ArrayList<>();
    private TenantDTO tenant;
    private ReminderStatus reminderStatus;
    private int id;

    public ServiceReminderDTO(final ServiceReminder serviceReminder) {
        this.createdBy = serviceReminder.getCreatedBy();
        this.createdDate = serviceReminder.getCreatedDate();
        this.active = Optional.ofNullable(serviceReminder.getActive()).orElse(Boolean.FALSE);
        this.modifiedBy = serviceReminder.getLastModifiedBy();
        this.remindUntil = serviceReminder.getRemindUntil();
        this.reminderStartDate = serviceReminder.getReminderStartDate();
        this.serviceVehicle = new ServiceVehicleDTO(serviceReminder.getServiceVehicle());
        this.serviceType = new ServiceTypeDTO(serviceReminder.getMdServiceType());
        this.vehicleServiceSchedule = new ServiceScheduleDTO(serviceReminder.getVehicleServiceSchedule());
        this.serviceReminderDetails = setServiceReminderDetails(serviceReminder.getServiceReminderDetails());
        this.tenant = tenant(serviceReminder.getMdTenant());
        this.reminderStatus = serviceReminder.getReminderStatus();
        this.id = serviceReminder.getId();
    }

    private TenantDTO tenant(MdTenant mdTenant) {
        return TenantDTO.TenantDTOBuilder.aTenantDTO()
                .withIdentifier(mdTenant.getTenantIdentifier())
                .withName(mdTenant.getTenantName())
                .withId(mdTenant.getId())
                .withOrganization(mdTenant.getMdOrganization().getOrgName()).build();
    }

    private List<ServiceReminderDetailsDTO> setServiceReminderDetails(
            List<ServiceReminderDetails> serviceReminderDetails) {
        if (serviceReminderDetails == null || serviceReminderDetails.isEmpty())
            return List.of();

        return serviceReminderDetails.stream().map(ServiceReminderDetailsDTO::new).collect(Collectors.toList());
    }
}
