package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.followup.AppointmentBookingScenario;
import com.automate.vehicleservices.api.model.followup.CustomerInterestedReasons;
import com.automate.vehicleservices.api.model.followup.FollowUpResultCapture;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.dto.ServiceReminderFollowUpActivityDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FollowUpClosingSynchronousActionService {
    private final ServiceAppointmentService serviceAppointmentService;
    private final ServiceReminderFollowUpResultingAppointmentService serviceReminderFollowUpResultingAppointmentService;

    public FollowUpClosingSynchronousActionService(
            ServiceAppointmentService serviceAppointmentService,
            ServiceReminderFollowUpResultingAppointmentService serviceReminderFollowUpResultingAppointmentService) {
        this.serviceAppointmentService = serviceAppointmentService;
        this.serviceReminderFollowUpResultingAppointmentService = serviceReminderFollowUpResultingAppointmentService;
    }

    public Object handleSynchronousFollowUpAction(
            FollowUpResultCapture followUpResultCapture,
            ServiceReminderFollowUpActivityDTO serviceReminderFollowUpActivityDTO) {
        final var reason = followUpResultCapture.getReason();
        CustomerInterestedReasons customerInterestedReasons = CustomerInterestedReasons.getEnum(reason).orElse(null);
        if (Objects.nonNull(customerInterestedReasons)) {
            return handleCustomerInterestedReasons(followUpResultCapture,
                    serviceReminderFollowUpActivityDTO, customerInterestedReasons);
        } else {
            throw new VehicleServicesException(String.format("Follow up Activity " +
                    "closed. But, Follow-up action failed due to Invalid reason  : %s", reason));
        }
    }

    private Object handleCustomerInterestedReasons(FollowUpResultCapture followUpResultCapture,
                                                   ServiceReminderFollowUpActivityDTO serviceReminderFollowUpActivityDTO,
                                                   CustomerInterestedReasons customerInterestedReasons) {
        switch (customerInterestedReasons) {
            case APPOINTMENT_BOOKING:
                // Add new vehicle to customer
                AppointmentBookingScenario appointmentBookingScenario = (AppointmentBookingScenario)
                        followUpResultCapture;
                final var serviceAppointmentResponse = serviceAppointmentService
                        .bookAppointment(appointmentBookingScenario.getAppointmentRequest(),
                                serviceReminderFollowUpActivityDTO.getTenant());

                // map it with follow up id
                serviceReminderFollowUpResultingAppointmentService
                        .addAppointmentFollowUpMapping(serviceAppointmentResponse.getResponseId(),
                                serviceReminderFollowUpActivityDTO.getFollowUpActivityId());
                return serviceAppointmentResponse;

        }
        return null;
    }
}
