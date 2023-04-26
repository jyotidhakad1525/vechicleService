package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.ServiceReminder;
import com.automate.vehicleservices.entity.ServiceReminderDetails;
import com.automate.vehicleservices.entity.enums.ReminderStatus;
import com.automate.vehicleservices.repository.ServiceReminderRepository;
import com.automate.vehicleservices.service.dto.ServiceReminderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Chandrashekar V
 */
@Component
public class ServiceReminderService {

    private final ServiceReminderRepository serviceReminderRepository;

    @Autowired
    public ServiceReminderService(ServiceReminderRepository serviceReminderRepository) {
        this.serviceReminderRepository = serviceReminderRepository;
    }

    @Transactional
    public List<ServiceReminderDTO> findActiveRemindersForVehicle(String regNumber) {

        final var serviceReminders =
                serviceReminderRepository.findByServiceVehicle_RegNumberAndReminderStatusIs(regNumber,
                        ReminderStatus.ACTIVE);

        return toDTOList(serviceReminders);
    }

    @Transactional
    public List<ServiceReminderDTO> findRemindersByTenantAndStatus(String tenant, ReminderStatus[] values) {
        final var serviceReminders =
                serviceReminderRepository.findByMdTenant_TenantIdentifierAndReminderStatusIn(tenant, values);
        return toDTOList(serviceReminders);
    }

    @Transactional
    public List<ServiceReminderDTO> findActiveRemindersByTenant(String tenant) {
        final var serviceReminders =
                serviceReminderRepository.findByMdTenant_TenantIdentifierAndReminderStatusIs(tenant,
                        ReminderStatus.ACTIVE);

        return toDTOList(serviceReminders);
    }

    @Transactional
    public List<ServiceReminderDTO> findRemindersByVehicle(String vehicleRegNumber, ReminderStatus[] reminderStatuses) {
        final var serviceReminders =
                serviceReminderRepository.findByServiceVehicle_RegNumberAndReminderStatusIn(vehicleRegNumber,
                        reminderStatuses);

        return toDTOList(serviceReminders);

    }

    @Transactional
    public List<ServiceReminderDTO> cancelServiceReminder(List<Integer> reminderIds) {
        final var serviceReminders = serviceReminderRepository.findAllById(reminderIds);

        for (ServiceReminder serviceReminder : serviceReminders) {
            serviceReminder.setReminderStatus(ReminderStatus.CANCELLED_BY_SYSTEM);
            for (ServiceReminderDetails serviceReminderDetail : serviceReminder.getServiceReminderDetails()) {
                serviceReminderDetail.setRemarks("Cancelled by system, due to vehicle update");
                serviceReminderDetail.setActive(false);
            }

        }
        final Iterable<ServiceReminder> reminders = serviceReminderRepository.saveAll(serviceReminders);

        return toDTOList(reminders);

    }

    public List<ServiceReminderDTO> toDTOList(Iterable<ServiceReminder> serviceReminders) {
        if (Objects.isNull(serviceReminders))
            return List.of();

        return StreamSupport.stream(serviceReminders.spliterator(), false)
                .map(ServiceReminderDTO::new).collect(Collectors.toList());
    }


}
