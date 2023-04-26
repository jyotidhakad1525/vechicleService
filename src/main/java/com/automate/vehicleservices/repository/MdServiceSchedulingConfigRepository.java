package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceSchedulingConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MdServiceSchedulingConfigRepository extends CrudRepository<MdServiceSchedulingConfig, Integer> {

    List<MdServiceSchedulingConfig> findByMdTenant_IsActiveTrue();

    MdServiceSchedulingConfig findByIdAndMdTenant_Id(int id, int tenantId);

    List<MdServiceSchedulingConfig> findAllByMdTenant_Id(int tenantId);

    void deleteByIdAndMdTenant_Id(int id, int tenantId);

    boolean existsByMdTenant_Id(int tenantId);
}
