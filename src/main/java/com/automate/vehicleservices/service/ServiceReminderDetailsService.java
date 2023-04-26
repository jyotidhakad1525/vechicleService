package com.automate.vehicleservices.service;

import com.automate.vehicleservices.repository.ServiceReminderDetailsRepository;
import com.automate.vehicleservices.service.dto.PageableResponse;
import com.automate.vehicleservices.service.dto.ServiceReminderDetailsDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceReminderDetailsService {
    private final ServiceReminderDetailsRepository serviceReminderDetailsRepository;

    public ServiceReminderDetailsService(ServiceReminderDetailsRepository repository) {
        this.serviceReminderDetailsRepository = repository;
    }


    @Transactional
    public PageableResponse<ServiceReminderDetailsDTO> remindersToBeSentToday(int currentPage, int batchSize) {
        final var remindersToBeSent =
                serviceReminderDetailsRepository.findByDateOfReminderAndActiveIsTrueAndSuccessIsFalse(LocalDate.now(),
                        PageRequest.of(currentPage, batchSize));

        final var collect = remindersToBeSent.get().map(ServiceReminderDetailsDTO::new).collect(Collectors.toList());

        if (remindersToBeSent.getTotalPages() == 0)
            return new PageableResponse<>();

        final var currentPageNumber = remindersToBeSent.getNumber() + 1;
        final boolean nextPageExists = currentPageNumber < remindersToBeSent.getTotalPages();
        return new PageableResponse(collect,
                nextPageExists, currentPageNumber, remindersToBeSent.getTotalPages(),
                remindersToBeSent.getTotalElements());
    }

    @Transactional
    public void updateStatusToSuccess(ServiceReminderDetailsDTO serviceReminderDetails, final String messageBody) {
        final var byId = serviceReminderDetailsRepository.findById(serviceReminderDetails.getId());
        byId.ifPresent(details -> {
            details.setActive(false);
            details.setSuccess(true);
            details.setMessageBody(messageBody);
            serviceReminderDetailsRepository.save(byId.get());
        });
    }

    @Transactional
    public void updateStatusToFailure(ServiceReminderDetailsDTO serviceReminderDetails, final String errorMessage) {
        final var byId = serviceReminderDetailsRepository.findById(serviceReminderDetails.getId());
        byId.ifPresent(details -> {
            details.setActive(true);
            details.setSuccess(false);
            details.setRemarks(errorMessage);
            serviceReminderDetailsRepository.save(byId.get());
        });
    }
}