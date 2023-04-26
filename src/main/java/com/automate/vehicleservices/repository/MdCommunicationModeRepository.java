package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.CommunicationMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MdCommunicationModeRepository extends CrudRepository<CommunicationMode, Integer> {
    List<CommunicationMode> findAllByMdTenant_Id(int tenantId);

    CommunicationMode findByIdAndMdTenant_Id(int id, int tenantId);
}
