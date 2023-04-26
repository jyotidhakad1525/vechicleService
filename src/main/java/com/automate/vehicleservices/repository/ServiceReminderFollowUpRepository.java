package com.automate.vehicleservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.automate.vehicleservices.entity.ServiceReminderFollowUp;
import com.automate.vehicleservices.entity.enums.FollowUpStatus;

@Repository
@Transactional
public interface ServiceReminderFollowUpRepository extends CrudRepository<ServiceReminderFollowUp, Integer> {

    List<ServiceReminderFollowUp> findByServiceVehicle_RegNumberAndStatusIn(final String vehicleRegNumber,
                                                                            final FollowUpStatus... status);

    List<ServiceReminderFollowUp> findByTenant_TenantIdentifierAndStatusIn(final String vehicleRegNumber,
                                                                           final FollowUpStatus... status);

    ServiceReminderFollowUp findByIdAndTenant_TenantIdentifier(final int id, final String tenantIdentifier);

    @Query(value = " select * from service_reminder_follow_up srfu where srfu.CUSTOMER_ID =:customerId order by LAST_MODIFIED_DATE desc limit 1", nativeQuery = true)
    ServiceReminderFollowUp findByCustomerId(int customerId);
}
