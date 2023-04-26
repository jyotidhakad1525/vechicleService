package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceReminderFollowUp;
import com.automate.vehicleservices.entity.enums.FollowUpStatus;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ServiceReminderFollowUpDTO {
    private int followUpId;
    private EmployeeDTO creAssigned;
    private FollowUpStatus status;
    private ServiceReminderDetailsDTO serviceReminderDetails;
    private CustomerDTO customer;
    private List<ServiceReminderFollowUpActivityDTO> serviceFollowUpActivities;
    private ServiceVehicleDTO serviceVehicle;
    private TenantDTO tenant;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public ServiceReminderFollowUpDTO(ServiceReminderFollowUp serviceReminderFollowUp) {
        this.followUpId = serviceReminderFollowUp.getId();
        this.creAssigned = new EmployeeDTO(serviceReminderFollowUp.getCre());
        this.status = serviceReminderFollowUp.getStatus();
        this.serviceReminderDetails = new ServiceReminderDetailsDTO(
                serviceReminderFollowUp.getServiceReminderDetails());
        this.customer = new CustomerDTO(serviceReminderFollowUp.getCustomer());
        if (CollectionUtils.isNotEmpty(serviceReminderFollowUp.getServiceFollowUpActivities()))
            this.serviceFollowUpActivities =
                    serviceReminderFollowUp.getServiceFollowUpActivities().stream()
                            .map(ServiceReminderFollowUpActivityDTO::new)
                            .collect(Collectors.toList());
        this.serviceVehicle = new ServiceVehicleDTO(serviceReminderFollowUp.getServiceVehicle());
        this.tenant = new TenantDTO(serviceReminderFollowUp.getTenant());
        this.createdBy = serviceReminderFollowUp.getCreatedBy();
        this.createdDate = serviceReminderFollowUp.getCreatedDate();
        this.lastModifiedBy = serviceReminderFollowUp.getLastModifiedBy();
        this.lastModifiedDate = serviceReminderFollowUp.getLastModifiedDate();
    }
}
