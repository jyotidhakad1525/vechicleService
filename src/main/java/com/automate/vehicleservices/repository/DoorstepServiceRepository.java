package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.DoorstepService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoorstepServiceRepository extends JpaRepository<DoorstepService, Integer>,
        JpaSpecificationExecutor<DoorstepService> {

}