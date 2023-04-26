package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceReminderFollowUpResultingAppointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ServiceReminderFollowUpResultingAppointmentRepository extends
        CrudRepository<ServiceReminderFollowUpResultingAppointment, Integer> {

    @Query(value = "insert into service_follow_up_resulting_appointment(APPOINTMENT_ID, FOLLOWUP_ACTIVITY_ID) VALUES " +
            "(:appointmentId, :followUpActivityId)", nativeQuery = true)
    void doMapping(@Param("appointmentId") int appointmentId, @Param("followUpActivityId") int followUpActivityId);
}
