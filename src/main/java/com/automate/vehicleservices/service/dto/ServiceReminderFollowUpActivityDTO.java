package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.LeadAllocationDetails;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceAppointment;
import com.automate.vehicleservices.entity.ServiceReminderFollowUp;
import com.automate.vehicleservices.entity.ServiceReminderFollowUpActivity;
import com.automate.vehicleservices.entity.VehicleServiceHistory;
import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.automate.vehicleservices.entity.enums.FollowUpActivityResult;
import com.automate.vehicleservices.entity.enums.FollowUpActivityStatus;
import com.automate.vehicleservices.entity.enums.FollowUpCategory;
import com.automate.vehicleservices.entity.enums.FollowUpReason;
import com.automate.vehicleservices.entity.enums.FollowUpStepStatus;
import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor

public class ServiceReminderFollowUpActivityDTO {
    private int followUpId;
    private int followUpActivityId;
    private FollowUpActivityStatus followUpActivityStatus;
    private FollowUpActivityResult followUpActivityResult;
    private FollowUpReason followUpReason;
    private String vehicleRegNumber;
    private String vehicleModel;
    private String customer;
    private String tenant;
    private EmployeeDTO cre;
    private String creRemarks;
    private String customerRemarks;
    private LocalDate startDate;
    private LocalDate endDate;
    private Object followUpActionResponse;
    private ServiceReminderFollowUpResultCaptureDTO serviceReminderFollowUpResultCaptureDTO;
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDate serviceDueDate;
    private String serviceType;
    private int serviceId;

    private RoDataDTO roDataDTO;

    private String category;

    // This two is pending
    private String location;
    private String serviceCenterCode;

    private String mobileNumber;
    private String pinCode;
    private String vin;
    private int millage;
    private LocalDate salesDate;

    private String firstName;

    private String lastName;

    private FollowUpStepStatus followUpStatus;

    private String lastService;
    private String lastSubService;
    private Integer lastMillage;

    private LocalDate bkgDate;

    private String bkgNumber;

    private Slot slotTime;

    public ServiceReminderFollowUpActivityDTO(ServiceReminderFollowUpActivity serviceReminderFollowUpActivity) {
        if (Objects.isNull(serviceReminderFollowUpActivity))
            return;
        this.followUpReason = serviceReminderFollowUpActivity.getFollowUpReason();
        this.followUpActivityId = serviceReminderFollowUpActivity.getId();
//        this.cre = new EmployeeDTO(serviceReminderFollowUpActivity.getCre());
        this.createdDate = serviceReminderFollowUpActivity.getCreatedDate();
        this.createdBy = serviceReminderFollowUpActivity.getCreatedBy();
        this.startDate = serviceReminderFollowUpActivity.getStartDate();
        this.endDate = serviceReminderFollowUpActivity.getEndDate();
        this.creRemarks = serviceReminderFollowUpActivity.getCreRemarks();
        this.customerRemarks = serviceReminderFollowUpActivity.getCustomerRemarks();
        this.followUpActivityResult = serviceReminderFollowUpActivity.getFollowUpActivityResult();
        this.followUpActivityStatus = serviceReminderFollowUpActivity.getFollowUpActivityStatus();
//        this.location = serviceReminderFollowUpActivity.getCreLocation();
//        this.serviceCenterCode = serviceReminderFollowUpActivity.getServiceCenterCode();
        this.followUpStatus = serviceReminderFollowUpActivity.getFollowUpStatus();
        this.serviceReminderFollowUpResultCaptureDTO =
                new ServiceReminderFollowUpResultCaptureDTO(
                        serviceReminderFollowUpActivity.getServiceReminderFollowUpResultCapture());
//        this.roDataDTO = new RoDataDTO(serviceReminderFollowUpActivity.getRoData());
        reminderFollowUpParentData(serviceReminderFollowUpActivity);

    }

    private void reminderFollowUpParentData(ServiceReminderFollowUpActivity serviceReminderFollowUpActivity) {
        final var serviceReminderFollowUp = serviceReminderFollowUpActivity.getServiceReminderFollowUp();
        if (Objects.nonNull(serviceReminderFollowUp)) {
            this.followUpId = serviceReminderFollowUp.getId();
            final var customer = serviceReminderFollowUp.getCustomer();
            if (Objects.nonNull(customer)) {
                this.firstName= customer.getFirstName();
                this.lastName = customer.getLastName();
                this.customer = customer.getFullName();
                this.mobileNumber = customer.getContactNumber();
                if (!customer.getCustomerAddresses().isEmpty())
                    this.pinCode = customer.getCustomerAddresses().get(0).getPin();
            }
            final var serviceVehicle = serviceReminderFollowUp.getServiceVehicle();
            if (Objects.nonNull(serviceVehicle)) {
               Optional<LeadAllocationDetails> leadAllocationDetails =  serviceVehicle.getLeadAllocationDetails().stream().filter(data->data.getStatus().equals(ActiveInActiveStatus.ACTIVE)).findFirst();
                if(leadAllocationDetails.isPresent()){
                    this.location = leadAllocationDetails.get().getCreLocation();
                    this.serviceCenterCode = leadAllocationDetails.get().getServiceCenterCode();
                    this.cre = new EmployeeDTO(leadAllocationDetails.get().getCreDetail());
                }

                List<VehicleServiceHistory> serviceHistories =  serviceVehicle.getVehicleServiceHistories();
                if(!serviceHistories.isEmpty()){
                    VehicleServiceHistory lastData = serviceHistories.get(0);
                    this.roDataDTO = new RoDataDTO(lastData.getRoData());
                    this.lastService = lastData.getMdServiceType().getMdServiceCategory().getCategoryName();
                    this.lastSubService = lastData.getMdServiceType().getServiceName();
                    this.lastMillage = lastData.getKmReading();
                }

                Optional<ServiceAppointment> appointments =  serviceVehicle.getAppointments().stream().filter(data->data.getStatus().equals(ServiceAppointmentStatus.BOOKED)).findFirst();
                if(appointments.isPresent()){
                    this.bkgDate = appointments.get().getServiceDate();
                    this.bkgNumber  = "BKG-"+appointments.get().getBOOKING_ID();
                    this.slotTime = new Slot(appointments.get().getServiceSlotAvailability());
                }

                this.vehicleRegNumber = serviceVehicle.getRegNumber();
                this.vehicleModel = serviceVehicle.getModel();
                this.vin = serviceVehicle.getVin();
                this.salesDate = serviceVehicle.getPurchaseDate();
                if (CollectionUtils.isNotEmpty(serviceVehicle.getVehicleKmTrackers()))
                    this.millage = serviceVehicle.getVehicleKmTrackers().get(0).getKmReading();
            }
            final var tenant = serviceReminderFollowUp.getTenant();
            if (Objects.nonNull(tenant)) {
                this.tenant = tenant.getTenantIdentifier();
            }

            serviceScheduleDetails(serviceReminderFollowUp);
        }
    }

    private void serviceScheduleDetails(ServiceReminderFollowUp serviceReminderFollowUp) {
        final var serviceReminderDetails = serviceReminderFollowUp.getServiceReminderDetails();
        if (Objects.nonNull(serviceReminderDetails)) {
            final var vehicleServiceSchedule = serviceReminderDetails.getServiceReminder()
                    .getVehicleServiceSchedule();
            this.serviceDueDate = vehicleServiceSchedule.getServiceDueDateRecommended();
            final var mdServiceType = vehicleServiceSchedule.getMdServiceType();
            if (Objects.nonNull(mdServiceType)) {
                this.serviceType = mdServiceType.getServiceName();
                this.serviceId = mdServiceType.getId();
                this.category = mdServiceType.getMdServiceCategory().getCategoryName();
            }
        }
    }

    public void setFollowUpActionResponse(Object followUpActionResponse) {
        this.followUpActionResponse = followUpActionResponse;
    }

    private TenantDTO tenant(MdTenant mdTenant) {
        return TenantDTO.TenantDTOBuilder.aTenantDTO()
                .withIdentifier(mdTenant.getTenantIdentifier())
                .withName(mdTenant.getTenantName())
                .withId(mdTenant.getId())
                .withOrganization(mdTenant.getMdOrganization().getOrgName()).build();
    }

}
