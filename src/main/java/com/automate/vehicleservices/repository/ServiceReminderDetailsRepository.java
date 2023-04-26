package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceReminderDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ServiceReminderDetailsRepository extends CrudRepository<ServiceReminderDetails, Integer> {

    /**
     * Returns Active reminders to be sent for the given date.
     *
     * @param dateOReminder
     * @param pageable
     * @return
     */
    Page<ServiceReminderDetails> findByDateOfReminderAndActiveIsTrueAndSuccessIsFalse(LocalDate dateOReminder,
                                                                                      Pageable pageable);

}