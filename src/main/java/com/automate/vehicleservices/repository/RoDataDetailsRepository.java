package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.RoDataDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoDataDetailsRepository extends JpaRepository<RoDataDetails, Integer> {
}
