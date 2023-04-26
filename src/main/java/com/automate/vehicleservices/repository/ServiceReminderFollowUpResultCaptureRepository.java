package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceReminderFollowUpResultCapture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ServiceReminderFollowUpResultCaptureRepository extends CrudRepository<ServiceReminderFollowUpResultCapture, Integer> {

}
