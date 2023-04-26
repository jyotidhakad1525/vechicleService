package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.entity.CommunicationMode;
import com.automate.vehicleservices.entity.ServiceReminderDetails;
import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.BaseTest;
import com.automate.vehicleservices.service.dto.CommunicationModeDTO;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

class EmailNotificationStrategyTest extends BaseTest {

    public static final String TEST_EMAIL = "testemail@automate.com";

    public static final String TEST_SMS_NUMBER = "9160002038";

    @Autowired
    CrudService crudService;

    @Autowired
    @Qualifier("EMAIL")
    private ReminderNotification emailReminderNotification;

    @Autowired
    @Qualifier("SMS")
    private ReminderNotification smsReminderNotification;

    //@Test
    @Transactional
    public void testEmailNotify() {

        final int reminderDetailsId = 25;
        Mockito.when(crudService.findById(reminderDetailsId, ServiceReminderDetails.class))
                .thenReturn(Optional.of(serviceReminderDetails(CommunicationModeEnum.EMAIL, TEST_EMAIL)));

        final var reminderDetailsOpt = crudService.findById(reminderDetailsId, ServiceReminderDetails.class);
        final var serviceReminderDetails = reminderDetailsOpt.get();
        final var serviceReminderDetailsDTO = new ServiceReminderDetailsDTO(serviceReminderDetails);

        emailReminderNotification.notify(serviceReminderDetailsDTO);
    }

    //@Test
    @Transactional
    public void testSMSNotify() {

        final int reminderDetailsId = 25;
        final var serviceReminderDetailsDTO1 = serviceReminderDetailsDTO();
        Mockito.when(crudService.findById(reminderDetailsId, ServiceReminderDetails.class))
                .thenReturn(Optional.of(serviceReminderDetails(CommunicationModeEnum.SMS, TEST_SMS_NUMBER)));

        final var reminderDetailsOpt = crudService.findById(reminderDetailsId, ServiceReminderDetails.class);

        final var serviceReminderDetails = reminderDetailsOpt.get();
        final var serviceReminderDetailsDTO = new ServiceReminderDetailsDTO(serviceReminderDetails);
        smsReminderNotification.notify(serviceReminderDetailsDTO);
    }

    private ServiceReminderDetails serviceReminderDetails(CommunicationModeEnum communicationModeEnum,
                                                          String communicationAddress) {
        final var serviceReminderDetails = new ServiceReminderDetails();
        serviceReminderDetails.setCommunicationAddress(communicationAddress);
        final var communicationMode = new CommunicationMode();
        communicationMode.setType(communicationModeEnum);
        serviceReminderDetails.setCommunicationMode(communicationMode);

        return serviceReminderDetails;
    }

    private ServiceReminderDetailsDTO serviceReminderDetailsDTO() {
        final var serviceReminderDetailsDTO1 = new ServiceReminderDetailsDTO();
        serviceReminderDetailsDTO1.setCommunicationAddress("sreeni.aluri@automate.com");
        final var communicationMode = new CommunicationMode();
        communicationMode.setType(CommunicationModeEnum.EMAIL);
        serviceReminderDetailsDTO1.setCommunicationMode(new CommunicationModeDTO(communicationMode));

        return serviceReminderDetailsDTO1;
    }


}