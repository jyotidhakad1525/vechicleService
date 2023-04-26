package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.ServiceReminder;
import com.automate.vehicleservices.entity.enums.ReminderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
public interface ServiceReminderRepository extends CrudRepository<ServiceReminder, Integer> {

    List<ServiceReminder> findByServiceVehicle_RegNumberAndReminderStatusIs(final String vehicleRegNumber,
                                                                            final ReminderStatus status);

    List<ServiceReminder> findByServiceVehicle_RegNumberAndReminderStatusIn(final String vehicleRegNumber,
                                                                            final ReminderStatus[] statuses);


    List<ServiceReminder> findByMdTenant_TenantIdentifierAndReminderStatusIn(final String tenant,
                                                                             final ReminderStatus[] statuses);

    List<ServiceReminder> findByMdTenant_TenantIdentifierAndReminderStatusIs(final String tenant,
                                                                             final ReminderStatus status);


}
