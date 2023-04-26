package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.TargetConfigurationParameterDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetConfParameterRepository extends JpaRepository<TargetConfigurationParameterDetails, Integer> {

    void deleteByTargetConfigurationDetailsId(Integer id);
}
