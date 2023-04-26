package com.automate.vehicleservices.reminder;

import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;
import com.automate.vehicleservices.repository.BaseTest;
import com.automate.vehicleservices.repository.dtoprojection.ServiceReminderPrefDTO;
import com.automate.vehicleservices.repository.dtoprojection.ServiceScheduleDTO;
import com.automate.vehicleservices.service.MdServiceReminderPrefService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class VehicleCoreReminderEngineTest extends BaseTest {

    @Mock
    MdServiceReminderPrefService mdServiceReminderPrefService;
    @Autowired
    private VehicleCoreReminderEngine vehicleCoreReminderEngine;

    //@Test
    public void testDeriveReminderDetails(String[] args) {
        final var after1Month =
                ServiceReminderPrefDTO.builder().id(5).expression(Expression.AFTER).durationValue(1).timeFrame(TimeFrame.MONTHS).build();
        final var after1Day =
                ServiceReminderPrefDTO.builder().id(5).expression(Expression.AFTER).durationValue(1).timeFrame(TimeFrame.DAYS).build();
        final var before10Days =
                ServiceReminderPrefDTO.builder().id(1).expression(Expression.BEFORE).durationValue(10).timeFrame(TimeFrame.DAYS).build();
        final var after10Days =
                ServiceReminderPrefDTO.builder().id(4).expression(Expression.AFTER).durationValue(10).timeFrame(TimeFrame.DAYS).build();
        final var before20Days =
                ServiceReminderPrefDTO.builder().id(2).expression(Expression.BEFORE).durationValue(20).timeFrame(TimeFrame.DAYS).build();
        final var before1Day =
                ServiceReminderPrefDTO.builder().id(3).expression(Expression.BEFORE).durationValue(1).timeFrame(TimeFrame.DAYS).build();
        final var before1Month =
                ServiceReminderPrefDTO.builder().id(5).expression(Expression.BEFORE).durationValue(1).timeFrame(TimeFrame.MONTHS).build();

        List<ServiceReminderPrefDTO> list = new ArrayList<>();
        list.add(before1Month);
        list.add(after1Month);
        list.add(after1Day);
        list.add(before10Days);
        list.add(after10Days);
        list.add(before20Days);
        list.add(before1Day);

        when(mdServiceReminderPrefService.fetchActiveReminderPreferencesByServiceType(anyInt())).thenReturn(list);

        vehicleCoreReminderEngine.initiateReminderFlow(ServiceScheduleDTO
                .builder().serviceDueDateRecommended(LocalDate.now()).build());

    }
}