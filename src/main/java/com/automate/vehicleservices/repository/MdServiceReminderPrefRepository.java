package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceReminderPref;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Repository
@Transactional
public interface MdServiceReminderPrefRepository extends CrudRepository<MdServiceReminderPref, Integer> {

    List<MdServiceReminderPref> findByIsActiveTrueAndMdServiceType_Id(final int id);

    MdServiceReminderPref findByIdAndMdTenant_Id(int id, int tenantId);

    void deleteByIdAndMdTenant_Id(int id, int tenantId);

    List<MdServiceReminderPref> findAllByMdTenant_Id(int tenantId);

}
