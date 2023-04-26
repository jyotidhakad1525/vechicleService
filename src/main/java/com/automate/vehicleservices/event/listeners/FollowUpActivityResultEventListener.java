package com.automate.vehicleservices.event.listeners;

import com.automate.vehicleservices.api.model.CustomerRequest;
import com.automate.vehicleservices.api.model.VehicleRequest;
import com.automate.vehicleservices.api.model.followup.*;
import com.automate.vehicleservices.event.FollowUpResultEvent;
import com.automate.vehicleservices.service.VehicleServiceHistoryService;
import com.automate.vehicleservices.service.dto.ServiceReminderFollowUpActivityDTO;
import com.automate.vehicleservices.service.facade.CustomerFacade;
import com.automate.vehicleservices.service.facade.VehicleFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@Slf4j
public class FollowUpActivityResultEventListener {


    private final VehicleFacade vehicleFacade;
    private final CustomerFacade customerFacade;
    private final VehicleServiceHistoryService serviceHistoryService;

    public FollowUpActivityResultEventListener(CustomerFacade customerFacade, VehicleFacade vehicleFacade,
                                               VehicleServiceHistoryService serviceHistoryService) {
        this.customerFacade = customerFacade;
        this.vehicleFacade = vehicleFacade;
        this.serviceHistoryService = serviceHistoryService;
    }

    /**
     * TODO: instead of switch , refactor design to implement handler for each case.
     *
     * @param followUpResultEvent
     */
    @EventListener(value = {FollowUpResultEvent.class})
    @Async
    @Transactional
    public void takeNextAction(FollowUpResultEvent followUpResultEvent) {
        final var followUpResultCapture = followUpResultEvent.getFollowUpResultCapture();
        final var serviceReminderFollowUpActivityDTO = followUpResultEvent.getServiceReminderFollowUpActivityDTO();
        final var tenantIdentifier = serviceReminderFollowUpActivityDTO.getTenant();


        final var reason = followUpResultCapture.getReason();
        CustomerNotInterestedReasons customerNotInterestedReasons =
                CustomerNotInterestedReasons.getEnum(reason).orElse(null);
        if (Objects.nonNull(customerNotInterestedReasons)) {
            handleCustomerNotInterestedReasons(customerNotInterestedReasons, followUpResultCapture,
                    serviceReminderFollowUpActivityDTO,
                    tenantIdentifier);
        }
    }

    private void handleCustomerNotInterestedReasons(CustomerNotInterestedReasons reason,
                                                    FollowUpResultCapture followUpResultCapture,
                                                    ServiceReminderFollowUpActivityDTO serviceReminderFollowUpActivityDTO, String tenantIdentifier) {
        try {
            switch (reason) {
                case VEHICLE_SOLD:
                    // Map New customer to existing vehicle
                    VehicleSoldScenario vehicleSoldScenario = (VehicleSoldScenario) followUpResultCapture;
                    final var customerDTO = customerFacade
                            .saveCustomer(vehicleSoldScenario.getCustomerRequest(), tenantIdentifier);
                    final var regNumber = serviceReminderFollowUpActivityDTO.getVehicleRegNumber();
                    vehicleFacade.mapVehicleToCustomer(customerDTO.getId(), regNumber);
                    log.info(String.format("New Customer added and mapped to the vehicle %s, after follow-up result " +
                            "event", regNumber));
                    break;
                case ALREADY_SERVICED:
                    // Add service history if provided.
                    AlreadyServicedScenario alreadyServicedScenario = (AlreadyServicedScenario) followUpResultCapture;
                    serviceHistoryService
                            .createServiceHistory(alreadyServicedScenario.getVehicleServiceHistoryRequest(),
                                    tenantIdentifier);
                    log.info("Vehicle history added, after follow-up result event");
                    break;
                case PURCHASED_ANOTHER_CAR:
                    // Add new vehicle to customer
                    PurchasedAnotherCarScenario purchasedAnotherCarScenario =
                            (PurchasedAnotherCarScenario) followUpResultCapture;

                    final var vehicleDetails = purchasedAnotherCarScenario.getVehicleDetails();
                    final CustomerRequest customerRequest = getCustomerRequest(serviceReminderFollowUpActivityDTO);
                    final var vehicleRequest = VehicleRequest.builder().vehicleDetails(vehicleDetails).customer(
                            customerRequest).build();
                    vehicleFacade.addNewVehicle(vehicleRequest, tenantIdentifier);
                    log.info("New vehicle added, after follow-up result event");
                    break;
                default:
                    log.info("No Handler implemented for %s", followUpResultCapture.getReason());
            }
        } catch (Exception ex) {
            log.error("Exception while handling follow up result event", ex);
        }
    }

    private CustomerRequest getCustomerRequest(ServiceReminderFollowUpActivityDTO serviceReminderFollowUpActivityDTO) {
        final var customer = serviceReminderFollowUpActivityDTO.getCustomer();

        return CustomerRequest.builder().contactNumber(customer).build();
    }
}
